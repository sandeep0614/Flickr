package sandeep.example.flickrbrowser

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : BaseActivity(){
   private val TAG="searchActivity"
    private  var searchView:SearchView?=null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
       activateToolbar(true)
        Log.d(TAG,".onCreate:ends")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG,".onCreateOptionsMenu: starts")
        menuInflater.inflate(R.menu.menu_search,menu)
        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView=menu.findItem(R.id.app_bar_search).actionView as SearchView
        val searchableInfo=searchManager.getSearchableInfo(componentName)
        searchView?.setSearchableInfo(searchableInfo)
//        Log.d(TAG,".onCreateOptionsMenu: $componentName")
//        Log.d(TAG,".onCreateOptionsMenu: hint is ${searchView?.queryHint}")
//        Log.d(TAG,".onCreateOptionsMenu: $searchableInfo")
        searchView?.isIconified=false
        searchView?.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG,".onQueryTextSubmit:called")
                val sharedPref=PreferenceManager.getDefaultSharedPreferences(applicationContext)
                sharedPref.edit().putString(FLICKR_QUERY,query).apply()
                searchView?.clearFocus()
                finish()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        searchView?.setOnCloseListener {
            finish()
            false
        }
        return true
    }
}