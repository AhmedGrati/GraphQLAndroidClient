package com.example.androidgraphql

import GetAllTodosQuery
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    private lateinit var todoAdapter : TodoRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTodoRecyclerView()

        displayTodos()

        setItemTouchHelper()

    }




    fun initTodoRecyclerView() {
        todo_list_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            todoAdapter = TodoRecyclerAdapter()
            adapter = todoAdapter
        }
    }

    fun displayTodos() {

        Thread(Runnable {
            val allTodosQuery = GetAllTodosQuery.builder().build()
            GraphQLClient.apolloClient.query(allTodosQuery)
                .enqueue(object : ApolloCall.Callback<GetAllTodosQuery.Data>(){
                    override fun onFailure(e: ApolloException) {
                        Log.d("ERRORTAG","error : ${e.message}")
                    }
                    override fun onResponse(response: Response<GetAllTodosQuery.Data>) {
                        Log.d("SUCCESSTAG","${response.data?.allTodos}")
                        this@MainActivity.runOnUiThread {
                            todoAdapter.submitList(response.data?.allTodos!!)
                        }
                    }
                })

        }).start()
    }

    fun setItemTouchHelper() {
        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0 ,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    Thread(Runnable {
                        val deletTodoQuery = DeleteTodoByIdMutation.builder()
                            .id(todoAdapter.getTodoAtIndex(viewHolder.adapterPosition).id()).build()

                        GraphQLClient.apolloClient.mutate(deletTodoQuery)
                            .enqueue(object : ApolloCall.Callback<DeleteTodoByIdMutation.Data>(){
                                override fun onFailure(e: ApolloException) {
                                    Log.d("ERRORTAG","${e.message}")
                                    this@MainActivity.runOnUiThread {
                                        Toast.makeText(this@MainActivity , "Error occurred while deleting todo !",Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onResponse(response: Response<DeleteTodoByIdMutation.Data>) {
                                    Log.d("SUCCESSTAG","${response.data}")
                                    todoAdapter.todoList = todoAdapter.todoList.toMutableList()
                                        .minusElement(todoAdapter.todoList[viewHolder.adapterPosition]).toMutableList()
                                    this@MainActivity.runOnUiThread {
                                        Toast.makeText(this@MainActivity , "Todo deleted successfully !",Toast.LENGTH_LONG).show()
                                        todoAdapter.notifyDataSetChanged()
                                    }


                                }

                            })
                    }).start()

                }
            }).attachToRecyclerView(todo_list_recycler_view)
    }
}
