package com.example.bt1.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
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

        userAdapter = new ArrayAdapter<User>(this, R.layout.list_item_user, userList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item_user, parent, false);
                }

                User user = getItem(position);

                TextView txtUsername = convertView.findViewById(R.id.txtUsername);
                TextView txtDetails = convertView.findViewById(R.id.txtDetails);
                ImageButton btnOptions = convertView.findViewById(R.id.btnOptions);

                assert user != null;
                txtUsername.setText(user.getUsername());
                txtDetails.setText(user.getFullname() + " - " + user.getEmail());

                btnOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(SearchUserActivity.this, v);
                        popupMenu.getMenuInflater().inflate(R.menu.menu_options, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                int itemId = item.getItemId();

                                if (itemId == R.id.menu_edit) {
                                    Intent intent = new Intent(SearchUserActivity.this, EditUserActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    return true;
                                } else if (itemId == R.id.menu_delete) {
                                    showDeleteConfirmationDialog(user);
                                    return true;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });

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
    }

    private void showDeleteConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchUserActivity.this);
        builder.setMessage("Are you sure you want to delete this user?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserList.delete(user);
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
    }
}
