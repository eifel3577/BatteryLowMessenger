package com.example.batterylowmessenger;

import android.databinding.BindingAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextBindings {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:inputText")
    public static void setInputText(EditText editText,String text) {
        editText.setText(text, TextView.BufferType.EDITABLE);
    }






    /*@SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(ListView listView, List<Task> items) {
        TasksAdapter adapter = (TasksAdapter) listView.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(items);
        }
    }*/
}
