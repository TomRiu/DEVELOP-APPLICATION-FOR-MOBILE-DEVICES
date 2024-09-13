package com.example.bt1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.bt1.R;
import com.example.bt1.model.User;
import com.example.bt1.model.UserList;

public class SearchUserActivity extends AppCompatActivity {

    private EditText txtSearch;
    private Button btnSearch;
    private ListView lvResults;
    private List<User> userList;
    private ArrayAdapter<User> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);

        txtSearch = findViewById(R.id.txtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        lvResults = findViewById(R.id.lvResults);

        userList = new ArrayList<>();

        userAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_2, android.R.id.text1, userList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
                }

                User user = getItem(position);

                TextView text1 = convertView.findViewById(android.R.id.text1);
                TextView text2 = convertView.findViewById(android.R.id.text2);

                assert user != null;
                text1.setText(user.getUsername());
                text2.setText(user.getFullname() + " - " + user.getEmail());

                return convertView;
            }
        };

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = txtSearch.getText().toString().trim();

                if (query.contains("@")) {
                    userList = UserList.findAllUsersByEmail(query);
                } else {
                    userList = UserList.findAllUsersByUsername(query);
                }

                if (userList.isEmpty()) {
                    Toast.makeText(SearchUserActivity.this, "No results found.", Toast.LENGTH_SHORT).show();
                    userAdapter.clear();
                } else {
                    userAdapter.clear();
                    userAdapter.addAll(userList);
                    userAdapter.notifyDataSetChanged();
                }
            }
        });

        lvResults.setAdapter(userAdapter);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = userList.get(position);

                if (getIntent().getBooleanExtra("isDeleteFunction", false)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchUserActivity.this);
                    builder.setMessage("Are you sure you want to delete this user?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserList.delete(selectedUser);
                            Toast.makeText(SearchUserActivity.this, "User deleted successfully!", Toast.LENGTH_SHORT).show();
                            btnSearch.callOnClick();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                    return;
                }
                Intent intent = new Intent(SearchUserActivity.this, EditUserActivity.class);
                intent.putExtra("user", selectedUser);
                startActivity(intent);
            }
        });
    }
}
