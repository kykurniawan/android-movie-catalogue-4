package com.rizky.submission.last.moviecatalogue.ui.favorite

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.ui.favorite.movie.MovieFavoriteFragment
import com.rizky.submission.last.moviecatalogue.ui.favorite.tvshow.TvShowFavoriteFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val tabTitle = intArrayOf(R.string.title_movie, R.string.title_tvshow)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieFavoriteFragment()
            1 -> fragment = TvShowFavoriteFragment()
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitle[position])
    }

    override fun getCount(): Int {
        return tabTitle.size
    }
}