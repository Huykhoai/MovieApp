package com.huynq.movieapp.base

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.huynq.movieapp.R
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.OutputStream

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
    protected fun onBack(){
        requireActivity().supportFragmentManager.popBackStack()
    }
    protected fun showDialog(title: String?, content: String?, onConfirm: Runnable) {
        val dialog = Dialog(requireContext())
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window!!.setBackgroundDrawable(resources.getDrawable(R.drawable.custom_dialog_background))
        val tvTitle = dialog.findViewById<TextView>(R.id.tv_title)
        val tvContent = dialog.findViewById<TextView>(R.id.tv_content)
        tvTitle.text = title
        tvContent.text = content

        val btnConfirm = dialog.findViewById<Button>(R.id.btn_okay)
        btnConfirm.setOnClickListener {
            onConfirm.run()
            dialog.dismiss()
        }

        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
    protected fun showSuccessDialog(content: String?) {
        val dialog = Dialog(requireContext())
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setBackgroundDrawable(resources.getDrawable(R.drawable.custom_dialog_background))
        dialog.setContentView(R.layout.layout_success_dialog)
        val tvContent = dialog.findViewById<TextView>(R.id.tv_success_content)
        tvContent.text = content
        val btnConfirm = dialog.findViewById<TextView>(R.id.btn_success_ok)
        btnConfirm.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
    fun convertImageToBase64(context: Context,imageUri: Uri): String{
        var base64Image = ""
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream)
            val bytes = outputStream.toByteArray()
            base64Image = Base64.encodeToString(bytes,Base64.DEFAULT)
        }catch (e: FileNotFoundException){
            throw RuntimeException(e)
        }
        return base64Image
    }
    fun convertBase64toImage(base64: String): Bitmap? {
        try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes,0, bytes.size)
        }catch (e: IllegalArgumentException){
            e.printStackTrace()
            return null
        }
    }
}