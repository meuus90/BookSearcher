package com.meuus90.booksearcher.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.doOnPreDraw
import com.meuus90.booksearcher.R
import com.meuus90.booksearcher.base.common.util.NumberTools
import com.meuus90.booksearcher.base.common.util.TimeTools
import com.meuus90.booksearcher.base.view.BaseFragment
import com.meuus90.booksearcher.base.view.ext.gone
import com.meuus90.booksearcher.base.view.ext.show
import com.meuus90.booksearcher.base.view.util.autoCleared
import com.meuus90.booksearcher.base.view.util.loadGlideImage
import com.meuus90.booksearcher.model.schema.book.BookItem
import com.meuus90.booksearcher.view.Caller
import com.meuus90.booksearcher.view.activity.MainActivity
import com.meuus90.booksearcher.viewmodel.book.BookDetailViewModel
import kotlinx.android.synthetic.main.fragment_book_detail.*
import javax.inject.Inject


class BookDetailFragment : BaseFragment() {
    companion object {
        const val KEY_BOOK = "KEY_BOOK"
    }

    private val mainActivity: MainActivity by lazy {
        activity as MainActivity
    }

    private lateinit var bookItem: BookItem
    private var acvView by autoCleared<View>()
    
    @Inject
    internal lateinit var bookDetailViewModel: BookDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        acvView = inflater.inflate(R.layout.fragment_book_detail, container, false)

        return acvView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        iv_thumbnail?.doOnPreDraw { startPostponedEnterTransition() }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = arguments
        bookItem = bundle?.getParcelable(KEY_BOOK)!!
        iv_thumbnail.transitionName = bookItem.databaseId.toString() + bookItem.isbn
        iv_thumbnail.loadGlideImage(bookItem.thumbnail, bookItem.databaseId, true)

        tv_title.text = bookItem.title

        tv_date.text =
            TimeTools.convertDateFormat(bookItem.datetime, TimeTools.ISO8601, TimeTools.YMD)

        tv_price.text = context.getString(
            R.string.currency_korean,
            NumberTools.convertToString(bookItem.price)
        )

        tv_publisher.text = bookItem.publisher

        tv_contents.text = getString(R.string.contents_format, bookItem.contents)

        iv_thumbs_up.isSelected = bookItem.thumbsUp

        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    setThumbsUp(true)

                    iv_thumbs_up_center.show()
                    val popAnim = AnimationUtils.loadAnimation(
                        context,
                        R.anim.anim_pop_from_zero
                    )
                    popAnim.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                        override fun onAnimationEnd(p0: Animation?) {
                            iv_thumbs_up_center.gone()
                        }

                        override fun onAnimationStart(p0: Animation?) {
                        }
                    })

                    iv_thumbs_up_center.startAnimation(popAnim)

                    return true
                }
            })

        v_contents.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            true
        }

        iv_thumbs_up.setOnClickListener {
            setThumbsUp(!bookItem.thumbsUp)
        }

        tv_next.setOnClickListener {
            val url = bookItem.url
            Caller.openUrlLink(context, url)
        }

        iv_back.setOnClickListener {
            mainActivity.onBackPressed()
        }
    }

    fun setThumbsUp(thumbsUp: Boolean) {
        bookItem.thumbsUp = thumbsUp
        iv_thumbs_up.isSelected = bookItem.thumbsUp
        bookDetailViewModel.updateBookItem(bookItem)

        iv_thumbs_up.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_pop))
    }
}