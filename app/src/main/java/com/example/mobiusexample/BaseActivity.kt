package com.example.mobiusexample

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.spotify.mobius.*
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.functions.Consumer
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer

abstract class BaseActivity<M : Parcelable, E, F> : AppCompatActivity(), Connectable<M, E> {

    companion object {
        private const val KEY_MODEL = "key_model"
    }

    private lateinit var controller: MobiusLoop.Controller<M, E>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
        controller = MobiusAndroid.controller(loop(), resolveModel(savedInstanceState))
        controller.connect(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_MODEL, controller.model)
    }

    override fun onPause() {
        super.onPause()
        controller.stop()
    }

    override fun onResume() {
        super.onResume()
        controller.start()
    }

    override fun onDestroy() {
        controller.disconnect()
        super.onDestroy()
    }

    override fun connect(output: Consumer<E>): Connection<M> {
        eventDispatcher(output)
        return object : Connection<M> {
            override fun accept(model: M) {
                render(model)
            }

            override fun dispose() {
                cleanUp()
            }
        }
    }

    @LayoutRes
    abstract fun layoutResId(): Int

    abstract fun eventDispatcher(output: Consumer<E>)

    abstract fun render(model: M)

    abstract fun initialModel(): M

    abstract fun update(): Update<M, E, F>

    abstract fun effectHandler(): ObservableTransformer<F, E>

    abstract fun cleanUp()

    protected open fun init(): Init<M, F> = Init {
        First.first(it)
    }

    private fun loop(): MobiusLoop.Factory<M, E, F> {
        return RxMobius
            .loop(update(), effectHandler())
            .init(init())
            .logger(AndroidLogger(getString(R.string.app_name)))
    }

    private fun resolveModel(savedInstanceState: Bundle?): M =
        savedInstanceState?.getParcelable(KEY_MODEL) ?: initialModel()
}