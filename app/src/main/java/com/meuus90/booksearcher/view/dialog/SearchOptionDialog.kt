package com.meuus90.booksearcher.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.meuus90.booksearcher.R
import com.meuus90.booksearcher.model.schema.book.BookRequest
import kotlinx.android.synthetic.main.dialog_search_option.*

class SearchOptionDialog(
    private val bookRequest: BookRequest,
    val onApply: (bookRequest: BookRequest) -> Unit
) : DialogFragment() {
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            statusBarColor = Color.TRANSPARENT

            setDimAmount(0.3f)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity?.layoutInflater?.inflate(R.layout.dialog_search_option, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        v_doc_sort_radio_group.check(bookRequest.sort.radioBtnResId)
        v_search_target_radio_group.check(bookRequest.target.radioBtnResId)

        v_doc_sort_radio_group.setOnCheckedChangeListener { _, idRes ->
            BookRequest.SortType.values().find { it.radioBtnResId == idRes }?.let {
                bookRequest.sort = it
            }
        }

        v_search_target_radio_group.setOnCheckedChangeListener { _, idRes ->
            BookRequest.TargetType.values().find { it.radioBtnResId == idRes }?.let {
                bookRequest.target = it
            }
        }

        tv_apply.setOnClickListener {
            onApply(bookRequest)
            dismiss()
        }

        view?.setOnClickListener { dismiss() }
    }
}