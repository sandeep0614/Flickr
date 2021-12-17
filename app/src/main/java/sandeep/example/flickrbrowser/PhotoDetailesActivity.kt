package sandeep.example.flickrbrowser

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.browse.*
import kotlinx.android.synthetic.main.content_photo_detailes.*

class PhotoDetailesActivity :BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detailes)
        setSupportActionBar(findViewById(R.id.toolbar))

        activateToolbar(true)
        val photo=intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        photo_title.text=resources.getString(R.string.photo_title_text,photo.title)
        photo_author.text=photo.author
        photo_tags.text="Tags: "+photo.tags
        Picasso.with(this).load(photo.link)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(photo_image)
    }
}