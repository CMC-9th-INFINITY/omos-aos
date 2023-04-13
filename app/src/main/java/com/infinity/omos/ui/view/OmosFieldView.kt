package com.infinity.omos.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.infinity.omos.R
import com.infinity.omos.databinding.ViewOmosFieldBinding


class OmosFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewOmosFieldBinding = ViewOmosFieldBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.colorPrimary))
    private val colorPrimary = typedArray.run {
        val color = getColor(0, 0)
        recycle()
        color
    }
    private val colorGray = ContextCompat.getColor(context, R.color.gray_05)

    var text: String
        get() = binding.etInput.text.toString()
        set(newText) {
            if (text != newText) {
                binding.etInput.setText(newText)
            }
        }

    init {
        initListener()
        getAttrs(attrs)
    }

    private fun initListener() {
        binding.ivEye.setOnClickListener {
            changePasswordVisible()
        }
    }

    private fun changePasswordVisible() {
        setPasswordVisible(binding.ivEye.isSelected.not())
    }

    private fun setPasswordVisible(isVisible: Boolean) {
        binding.ivEye.isSelected = isVisible
        binding.etInput.transformationMethod = if (isVisible) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
        binding.etInput.setSelection(text.length)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val types = context.obtainStyledAttributes(attrs, R.styleable.OmosFieldView)
        setTypedArray(types)
    }

    private fun setTypedArray(types: TypedArray) {
        val title = types.getText(R.styleable.OmosFieldView_title)
        setTitle(title.toString())

        val hint = types.getText(R.styleable.OmosFieldView_hint)
        setHint(hint.toString())

        val inputType = types.getInt(R.styleable.OmosFieldView_android_inputType, -1)
        setInputType(inputType)

        val length = types.getInt(R.styleable.OmosFieldView_android_maxLength, Int.MAX_VALUE)
        setMaxLength(length)

        types.recycle()
    }

    private fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    private fun setHint(hint: String) {
        binding.etInput.hint = hint
    }

    private fun setInputType(type: Int) {
        binding.etInput.inputType = type

        when (type) {
            InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> {

            }

            InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                showPasswordToggle()
            }
        }
    }

    private fun showPasswordToggle() {
        binding.ivEye.visibility = View.VISIBLE
    }

    private fun setMaxLength(length: Int) {
        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = LengthFilter(length)
        binding.etInput.filters = fArray
    }

    fun setShowErrorMsg(state: Boolean, msg: String) {
        binding.constraintInput.isActivated = state
        binding.tvMsg.text = msg

        if (state) {
            val shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake)
            binding.constraintLayout.startAnimation(shakeAnimation)
            binding.tvMsg.setTextColor(colorPrimary)
        } else {
            binding.tvMsg.setTextColor(colorGray)
        }
    }

    fun setEditTextEnabled(enabled: Boolean) {
        binding.etInput.isEnabled = enabled
    }

    fun setOnFocusChangeListener(listener: (Boolean) -> Unit) {
        binding.etInput.setOnFocusChangeListener { _, b ->
            listener.invoke(b) // 포커스가 떠날 때
        }
    }

    fun setOnTextChangeListener(listener: (String) -> Unit) {
        binding.etInput.doAfterTextChanged {
            listener.invoke(it.toString())
        }
    }

    /**
     *  android:saveEnabled = false
     *  editText의 값이 마지막에 입력한 값으로 동일하게 복원되는 문제 해결
     */
    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("instanceState", super.onSaveInstanceState())
        bundle.putString("text", binding.etInput.text.toString())
        bundle.putBoolean("passwordVisible", binding.ivEye.isSelected)
        bundle.putBoolean("isFocused", binding.etInput.hasFocus())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            binding.etInput.setText(state.getString("text"))
            setPasswordVisible(state.getBoolean("passwordVisible"))

            if (state.getBoolean("isFocused")) {
                binding.etInput.requestFocus()
            }

            super.onRestoreInstanceState(state.getParcelable("instanceState"))
            return
        }
        super.onRestoreInstanceState(state)
    }
}