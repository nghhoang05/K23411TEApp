package com.example.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k23411teapp.R;
import com.example.models.Contact;

import java.util.List;

/**
 * Custom adapter hiển thị danh sách Contact với layout card view.
 * Avatar chữ cái đầu được tô màu dựa trên hashCode của tên để
 * mỗi người có màu nhất quán.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    private final Activity context;

    // Bảng màu avatar đa dạng
    private static final int[] AVATAR_COLORS = {
            0xFF1E88E5, // blue
            0xFF43A047, // green
            0xFFE53935, // red
            0xFF8E24AA, // purple
            0xFFF4511E, // deep orange
            0xFF00ACC1, // cyan
            0xFFFFB300, // amber
            0xFF6D4C41, // brown
            0xFF039BE5, // light blue
            0xFF00897B  // teal
    };

    public ContactAdapter(@NonNull Activity context, @NonNull List<Contact> contacts) {
        super(context, R.layout.item_contact, contacts);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
            holder = new ViewHolder();
            holder.tvAvatar       = convertView.findViewById(R.id.tvAvatar);
            holder.tvName         = convertView.findViewById(R.id.tvContactName);
            holder.tvEmail        = convertView.findViewById(R.id.tvContactEmail);
            holder.tvPhone        = convertView.findViewById(R.id.tvContactPhone);
            holder.tvKey          = convertView.findViewById(R.id.tvContactKey);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact contact = getItem(position);
        if (contact == null) return convertView;

        // Hiển thị chữ cái đầu của tên làm avatar
        String name = contact.getName() != null ? contact.getName() : "?";
        String firstLetter = name.isEmpty() ? "?" : String.valueOf(name.charAt(0)).toUpperCase();
        holder.tvAvatar.setText(firstLetter);

        // Màu avatar dựa vào hashCode của tên (nhất quán)
        int colorIndex = Math.abs(name.hashCode()) % AVATAR_COLORS.length;
        holder.tvAvatar.getBackground().mutate().setTint(AVATAR_COLORS[colorIndex]);

        holder.tvName.setText(name);
        holder.tvEmail.setText(contact.getEmail() != null ? contact.getEmail() : "—");
        holder.tvPhone.setText(contact.getPhone() != null ? contact.getPhone() : "—");
        holder.tvKey.setText(contact.getKey());

        return convertView;
    }

    /** ViewHolder pattern để tránh gọi findViewById mỗi lần scroll */
    static class ViewHolder {
        TextView tvAvatar;
        TextView tvName;
        TextView tvEmail;
        TextView tvPhone;
        TextView tvKey;
    }
}
