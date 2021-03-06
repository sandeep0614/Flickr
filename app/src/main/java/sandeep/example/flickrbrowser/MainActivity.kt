package sandeep.example.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import  kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity(),GetRawData.onDownloadComplete,GetFlickrJsonData.OnDataAvailable,RecyclerItemClickListener.OnRecyclerClickListener {
    private val TAG="MainActivity"
   private val flickrRecyclerViewAdapter=FlickrRecyclerViewAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        activateToolbar(false)

       recycler_view.layoutManager=LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this,recycler_view,this))
        recycler_view.adapter=flickrRecyclerViewAdapter


//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        Log.d(TAG,"onCreate ends")

    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG,".onItemClick:starts")
        Toast.makeText(this,"Normal tap at position $position",Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG,".onItemLOngClick: starts")
        //Toast.makeText(this,"Long tap at position $position",Toast.LENGTH_SHORT).show()
        val photo=flickrRecyclerViewAdapter.getPhoto(position)
        if(photo!=null)
        {
            val intent=Intent(this,PhotoDetailesActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER,photo)
            startActivity(intent)
        }
    }

    private fun createUri(baseURL:String, searchCriteria:String, lang:String, matchAll:Boolean):String{
        Log.d(TAG,"createUri starts")
        return Uri.parse(baseURL).
                buildUpon().
                appendQueryParameter("tags",searchCriteria).
                appendQueryParameter("tagmode",if(matchAll) "ALL" else "ANY").
                appendQueryParameter("lang",lang).
                appendQueryParameter("format","json").
                appendQueryParameter("nojsoncallback","1").
                build().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG,"onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG,"onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this,SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
//    companion object{
//        private const val TAG="MainActivity"
//    }
    override fun onDownloadComplete(data:String,status:DownloadStatus){
    if(status==DownloadStatus.OK){
        Log.d(TAG,"onDownloadComplete called, data is $data")
        val getFlickrJsonData=GetFlickrJsonData(this)
        getFlickrJsonData.execute(data)
    }
    else
    {
        Log.d(TAG,"onDownloadComplete failed with status $status. Error message is $data")
    }
}
   override fun onDataAvailable(data:List<Photo>){
         Log.d(TAG,"onDataAvailable called")
       flickrRecyclerViewAdapter.loadNewData(data)
       Log.d(TAG,"onDataAvailable ends")
    }
    override fun onError(exception:Exception){
        Log.d(TAG,"onError called with ${exception.message}")
    }

    override fun onResume() {
        Log.d(TAG,".onResume:starts")
        super.onResume()
        val sharedPref=PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult=sharedPref.getString(FLICKR_QUERY,"")
        if(queryResult!!.isNotEmpty()){
            val url=createUri("https://www.flickr.com/services/feeds/photos_public.gne",queryResult,"en-us",true)
            val getRawData=GetRawData(this)
            // getRawData.setDownloadCompleteListener(this)
            getRawData.execute(url)
        }
    }
}