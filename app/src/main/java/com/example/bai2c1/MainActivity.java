package com.example.bai2c1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.model.SanPham;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    ListView lvSanPham;
    ArrayAdapter<SanPham> sanPhamAdapter;
    SanPham selectedSanPham = null;
    ArrayList<SanPham> dsNguon = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvSanPham.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSanPham = sanPhamAdapter.getItem(position);
                return false;
            }
        });
        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSanPham = sanPhamAdapter.getItem(position);
            }
        });
    }

    private void addControls() {
        lvSanPham = findViewById(R.id.lvSanPham);
        sanPhamAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);
        lvSanPham.setAdapter(sanPhamAdapter);
        sanPhamAdapter.add(new SanPham("sp1","SamSung20","10000"));
        sanPhamAdapter.add(new SanPham("sp2","SamSung40","10000"));
        sanPhamAdapter.add(new SanPham("sp3","SamSung50","10000"));
        sanPhamAdapter.add(new SanPham("sp4","SamSung60","10000"));
        sanPhamAdapter.add(new SanPham("sp5","SamSung70","10000"));
        for(int i=0;i<sanPhamAdapter.getCount();i++)
        {
            dsNguon.add(sanPhamAdapter.getItem(i));
        }
        registerForContextMenu(lvSanPham);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuSua:
                HienThiManHinhSua();
                break;
            case R.id.menuXoa:
                sanPhamAdapter.remove(selectedSanPham);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void HienThiManHinhSua() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.edit_layout);
        EditText edtMa1 = dialog.findViewById(R.id.edtMa1);
        EditText edtTen1 = dialog.findViewById(R.id.edtTen1);
        EditText edtGia1 = dialog.findViewById(R.id.edtGia1);
        Button btnXoa = dialog.findViewById(R.id.btnXoa);
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGia1.setText("");
                edtMa1.setText("");
                edtTen1.setText("");
                edtMa1.requestFocus();
            }
        });
        Button btnLuu1 = dialog.findViewById(R.id.btnLuu1);
        btnLuu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSanPham.setMa(edtMa1.getText().toString());
                selectedSanPham.setGia(edtGia1.getText().toString());
                selectedSanPham.setTen(edtTen1.getText().toString());
                sanPhamAdapter.notifyDataSetChanged();
                dialog.dismiss();
                dsNguon.clear();
                for(int i=0;i<sanPhamAdapter.getCount();i++)
                {
                    dsNguon.add(sanPhamAdapter.getItem(i));
                }
                Toast.makeText(MainActivity.this,"Luu thanh cong",Toast.LENGTH_LONG).show();
            }
        });
        edtGia1.setText(selectedSanPham.getGia());
        edtTen1.setText(selectedSanPham.getTen());
        edtMa1.setText(selectedSanPham.getMa());
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        MenuItem menuSearch = menu.findItem(R.id.menuSearch);
        SearchView searchView  = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty())
                {
                    sanPhamAdapter.clear();
                    sanPhamAdapter.addAll(dsNguon);
                }
                else {
                    ArrayList<SanPham> dsTim = new ArrayList<>();
                    for(SanPham sp: dsNguon)
                    {
                        if(sp.getMa().contains(newText) || sp.getTen().contains(newText))
                        {
                            dsTim.add(sp);
                        }
                    }
                    sanPhamAdapter.clear();
                    sanPhamAdapter.addAll(dsTim);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuThem:
                HienThiManHinhThem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void HienThiManHinhThem() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.detail_layout);
        dialog.setTitle("Them san pham");
        EditText edtMa = dialog.findViewById(R.id.edtMa1);
        EditText edtTen = dialog.findViewById(R.id.edtTen1);
        EditText edtGia = dialog.findViewById(R.id.edtGia1);
        Button btnLuu = dialog.findViewById(R.id.btnLuu1);
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SanPham sp = new SanPham();
                sp.setMa(edtMa.getText().toString());
                sp.setTen(edtTen.getText().toString());
                sp.setGia(edtGia.getText().toString());
                sanPhamAdapter.add(sp);
                dialog.dismiss();
                dsNguon.clear();
                for(int i=0;i<sanPhamAdapter.getCount();i++)
                {
                    dsNguon.add(sanPhamAdapter.getItem(i));
                }
            }
        });
        dialog.show();
    }
}