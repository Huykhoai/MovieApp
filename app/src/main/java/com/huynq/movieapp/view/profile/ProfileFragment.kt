package com.huynq.movieapp.view.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentProfileBinding
import com.huynq.movieapp.databinding.OpenPictureBinding
import com.huynq.movieapp.model.UserResponse
import com.huynq.movieapp.view.changepass.ChangePasswordFragment
import com.huynq.movieapp.view.login.LoginFragment
import com.huynq.movieapp.view.watchlist.WatchListFragment
import com.huynq.movieapp.viewmodel.UserModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: UserModel by viewModels()
    private var currentPhotoPath:String? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_TAKE_PHOTO = 2
    private var imageSelected: Uri? = null
    private var userResponse : UserResponse? = null
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.myProfile = viewModel
        binding!!.lifecycleOwner =this
        binding!!.proccessBar.visibility = View.VISIBLE
        super.onViewCreated(view, savedInstanceState)
        observer()
    }

    private fun observer() {
        val sharePreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val userJson = sharePreferences.getString("user", null)
        if (userJson != null) {
            val user = Gson().fromJson(userJson, UserResponse::class.java)
            viewModel.get_user(user.user._id)
        }
        viewModel.user.observe(viewLifecycleOwner) {
            binding!!.apply {
                if(it != null){
                    userResponse = it
                    proccessBar.visibility = View.GONE
                    Log.d("Huy", "observer:${it.user} ")
                    Glide.with(requireActivity()).load(convertBase64toImage(userResponse!!.user.avatar)).into(binding!!.imgProfile)
                    initView()
                }
            }
        }
        viewModel.change_avatar.observe(viewLifecycleOwner){
            binding!!.apply {
               it.onSuccess {
                   userResponse = it
                   Glide.with(requireActivity()).load(convertBase64toImage(userResponse!!.user.avatar)).into(binding!!.imgProfile)
               }
                   .onFailure {
                       showSuccessDialog(it.message)
                   }
            }
        }

    }

    private fun initView() {
        binding!!.apply {
            btnWatchList.setOnClickListener{
                openScreen(WatchListFragment(),true)
            }
            btnChangePass.setOnClickListener {
                openScreen(ChangePasswordFragment(), true)
            }
            requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                }

            })
            btnLogout.setOnClickListener {
                val sharePreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
                sharePreferences.edit().clear().apply()
                openScreen(LoginFragment(), false)
            }
            btnAddImage.setOnClickListener {
                bottomSheet()
            }
        }
    }
    private fun openCamera(){
        if(ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED){
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var file: File? = null
            try {
                file = createImageFile()
            }catch(e: IOException){
                e.printStackTrace()
            }
            if(file != null){
                val photoUri =FileProvider.getUriForFile(requireActivity(),"com.huynq.movieapp.fileprovider",file)
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intentCamera,REQUEST_IMAGE_CAPTURE)
            }
        }else{
            requestPermissions(arrayOf<String>(android.Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File{
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(Date())
        val imageFileName = "JPG_"+timeStamp+"_"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName,".jpg", storageDir)
        currentPhotoPath = image.getAbsolutePath()
        return image
    }
    private fun onpenGallary(){
        if (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,REQUEST_TAKE_PHOTO)
        }else{
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_TAKE_PHOTO)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera()
            }else{
                showSuccessDialog("Camera permission denied")
            }
        }else if(requestCode == REQUEST_TAKE_PHOTO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onpenGallary()
            } else {
                showSuccessDialog("Storage permission denied")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE &&
            resultCode == AppCompatActivity.RESULT_OK &&
            data != null){
            imageSelected = data.data
            val base64 = convertImageToBase64(requireActivity().application,imageSelected!!)
            viewModel.change_avatar(userResponse!!.user.email, base64!!)
        }else if(requestCode == REQUEST_TAKE_PHOTO &&
            resultCode == AppCompatActivity.RESULT_OK &&
            data != null){
            imageSelected = data.data
            val base64 = convertImageToBase64(requireActivity().application,imageSelected!!)
            viewModel.change_avatar(userResponse!!.user.email, base64!!)
        }
    }
    private fun bottomSheet(){
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val binding = OpenPictureBinding.inflate(layoutInflater)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setContentView(binding.root)
        binding!!.apply {
            tvOpenCamera.setOnClickListener {
                openCamera()
                bottomSheetDialog.dismiss()
            }
            tvOpenGallery.setOnClickListener {
                onpenGallary()
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()

    }
}