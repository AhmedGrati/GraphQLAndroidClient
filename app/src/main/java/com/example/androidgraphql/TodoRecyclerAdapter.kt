package com.example.androidgraphql

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_todo_list_item.view.*


class TodoRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var todoList:MutableList<GetAllTodosQuery.GetAllTodo> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_todo_list_item , parent , false)
        )
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is TodoViewHolder -> {
                holder.bind(todoList[position])
            }
        }
    }

    fun submitList(todos: MutableList<GetAllTodosQuery.GetAllTodo>) {

        todoList = todos

        notifyDataSetChanged()
    }

    fun getTodoAtIndex(index: Int): GetAllTodosQuery.GetAllTodo {
        return todoList[index]
    }

    class TodoViewHolder constructor(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var todoTitle = itemView.todo_title
        var todoDescription = itemView.todo_description
        var todoDuration = itemView.todo_duration

        fun bind(todo: GetAllTodosQuery.GetAllTodo) {
            todoTitle.text = "Title : ${todo.title()}"
            todoDescription.text = "Description : ${todo.description()}"
            todoDuration.text = "Duration : ${todo.duration().toString()} minutes"
        }
    }
}