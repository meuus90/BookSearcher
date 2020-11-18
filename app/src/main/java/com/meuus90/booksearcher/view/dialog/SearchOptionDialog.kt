package com.meuus90.booksearcher.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.meuus90.booksearcher.R
import com.meuus90.booksearcher.base.view.ext.setDefaultWindowTheme
import com.meuus90.booksearcher.model.schema.book.BookRequest
import kotlinx.android.synthetic.main.dialog_search_option.*

class SearchOptionDialog(
    val bookRequest: BookRequest,
    val onApply: (bookRequest: BookRequest) -> Unit
) : DialogFragment() {
    override fun onStart() {
        super.onStart()
        dialog?.setDefaultWindowTheme()
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

        v_doc_sort_radio_group.check(bookRequest.sort.idRes)
        v_search_target_radio_group.check(bookRequest.target.idRes)

        v_doc_sort_radio_group.setOnCheckedChangeListener { radioGroup, idRes ->
            BookRequest.SortType.values().find { it.idRes == idRes }?.let {
                bookRequest.sort = it
            }
        }

        v_search_target_radio_group.setOnCheckedChangeListener { radioGroup, idRes ->
            BookRequest.TargetType.values().find { it.idRes == idRes }?.let {
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