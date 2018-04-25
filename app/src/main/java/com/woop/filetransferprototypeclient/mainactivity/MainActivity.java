package com.woop.filetransferprototypeclient.mainactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.woop.filetransferprototypeclient.R;
import com.woop.filetransferprototypeclient.config.OptionsData;
import com.woop.filetransferprototypeclient.config.OptionsDataLoader;
import com.woop.filetransferprototypeclient.mainactivity.fragments.ChangePasswordFragment;
import com.woop.filetransferprototypeclient.mainactivity.fragments.DownloadFileFragment;
import com.woop.filetransferprototypeclient.mainactivity.fragments.UploadFileFragment;

import com.mikepenz.materialdrawer.DrawerBuilder;


public class MainActivity extends AppCompatActivity {


    private Drawer drawer = null;
    private AccountHeader header = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDrawer);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final IProfile profile = new ProfileDrawerItem().withName(OptionsData.getInstance().getLogin()).withEmail(OptionsData.getInstance().getServer()).withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"));
        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withProfileImagesVisible(false)
                .addProfiles(profile)
                .withSavedInstance(savedInstanceState)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.header)
                .withAccountHeader(header)
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.drawer_item_main_section_header),
                        new PrimaryDrawerItem().withName(R.string.title_upload_file_fragment).withIcon(FontAwesome.Icon.faw_upload).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.title_download_file_fragment).withIcon(FontAwesome.Icon.faw_download).withIdentifier(2),
                        new SectionDrawerItem().withName(R.string.drawer_item_additional_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(3),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_change_password).withIcon(FontAwesome.Icon.faw_exchange).withIdentifier(4),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_exit).withIcon(FontAwesome.Icon.faw_close).withIdentifier(5)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new UploadFileFragment()).commit();
                            } else if (drawerItem.getIdentifier() == 2) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new DownloadFileFragment()).commit();
                            } else if (drawerItem.getIdentifier() == 3) {
                                Toast.makeText(MainActivity.this, "О программе", Toast.LENGTH_SHORT).show();

                            } else if (drawerItem.getIdentifier() == 4) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ChangePasswordFragment()).commit();

                            } else if (drawerItem.getIdentifier() == 5) {
                                Toast.makeText(MainActivity.this, "Выход" , Toast.LENGTH_SHORT).show();
                                deleteData();
                                finish();
                            }


                        }

                        return false;
                    }
                })
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        //drawer.setSelection(1);
        drawer.openDrawer();
    }

    private void deleteData() {
        OptionsData.removeInstance();
        SharedPreferences preferences = this.getSharedPreferences(OptionsDataLoader.APP_PREFERENCES, Context.MODE_PRIVATE);
        OptionsDataLoader loader = new OptionsDataLoader(preferences);
        loader.deleteData();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen())
            drawer.closeDrawer();
    }


}
