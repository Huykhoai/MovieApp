package com.huynq.movieapp.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.huynq.movieapp.R

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var loadingDialog: Dialog? = null
    protected var binding :T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        return binding?.root
    }
    protected abstract fun getFragmentBinding(layoutInflater: LayoutInflater,container: ViewGroup?): T

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    protected fun openScreen(fragment: Fragment, addtoBackStack : Boolean){
        val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            transaction?.replace(R.id.frameLayout_container,fragment)
        if(addtoBackStack){
            transaction?.addToBackStack(null)
        }
        transaction?.commit()
    }
}