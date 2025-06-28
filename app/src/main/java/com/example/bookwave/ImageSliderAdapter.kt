import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.bookwave.R

class ImageSliderAdapter(private val imageList: ArrayList<Int> , private val viewPager2 : ViewPager2 )
    : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>(){

    class ImageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val imageVIew : ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image,parent,false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageVIew.setImageResource(imageList[position])
        if(position == imageList.size-1){
            viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable{
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }

}
