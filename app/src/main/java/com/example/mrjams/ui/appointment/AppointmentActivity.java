package com.example.mrjams.ui.appointment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.MainActivity;
import com.example.mrjams.R;
import com.example.mrjams.ui.SessionClass;
import com.example.mrjams.ui.mail.MailAppointmentFragment;
import com.google.protobuf.StringValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity implements ServicesChooser.DialogListener{

    TextView textServices;
    CardView selectServices;
    CardView cardPackage;
    CardView cardTime;

    ArrayList<Packages> packagesArrayList = new ArrayList<>();
    ArrayList<String> stringPackage = new ArrayList<>();
    ArrayAdapter<String> packageArray;


    DatePickerDialog datePicker;
    TimePickerDialog timePickerDialog;

    EditText tboxFname, tboxMname, tboxLname, tboxAge, tboxPhone, tboxAL1, tboxAL2, tboxDate, tboxDateTime, tboxTime;
//    EditText cardTime;

    CheckBox cBoxServices, cBoxPackages;
    Button btnAppSubmit;
    AutoCompleteTextView tboxGender;
    Spinner sprPackage;
    String packages_id = "0";
    String SERVICE_IDS = "0", user_id;
    String idSunday, idMonday, idTuesday, idWednesday, idThursday, idFriday, idSaturday;

    String Fname, Mname, Lname, Age, Gender, Phone, AddLine1, AddLine2, DateToday, DateTime, clinic_id;
    String restrictedDay;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        clinic_id = getIntent().getStringExtra("clinic_id");
        user_id = getIntent().getStringExtra("user_id");
        sprPackage = findViewById(R.id.sprPackage);
        tboxFname = findViewById(R.id.tboxAppFname);
        tboxMname = findViewById(R.id.tboxAppMname);
        tboxLname = findViewById(R.id.tboxAppLname);
        tboxAge = findViewById(R.id.tboxAppAge);
        tboxPhone = findViewById(R.id.tboxAppNumber);
        tboxAL1 = findViewById(R.id.tboxAppAL1);
        tboxAL2 = findViewById(R.id.tboxAppAL2);
        tboxDate = findViewById(R.id.tboxAppDate);
        btnAppSubmit = findViewById(R.id.btnAppSubmit);
        tboxGender = findViewById(R.id.tboxAppGender);

        tboxDateTime = findViewById(R.id.tboxAppDateTime);
        tboxTime = findViewById(R.id.tboxAppTime);

        cBoxServices = findViewById(R.id.cBoxService);
        cBoxPackages = findViewById(R.id.cBoxPackage);

        textServices = findViewById(R.id.tvServices);
        selectServices = findViewById(R.id.selectServices);

        cardTime = findViewById(R.id.cardTime);
        cardPackage = findViewById(R.id.cardPackage);

        String dateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tboxDate.setText(dateToday);

        cBoxServices.setOnCheckedChangeListener(new MyCheckedChangeListener());
        cBoxPackages.setOnCheckedChangeListener(new MyCheckedChangeListener());

        tboxDateTime.setInputType(InputType.TYPE_NULL);
        tboxTime.setInputType(InputType.TYPE_NULL);


        ArrayAdapter<CharSequence> gender = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_dropdown_item_1line);
        tboxGender.setAdapter(gender);

        if (getIntent().hasExtra("customer_fname")){
            tboxFname.setText(getIntent().getStringExtra("customer_fname"));
            tboxMname.setText(getIntent().getStringExtra("customer_mname"));
            tboxLname.setText(getIntent().getStringExtra("customer_lname"));
            tboxGender.setText(getIntent().getStringExtra("customer_gender"));
            tboxAge.setText(getIntent().getStringExtra("customer_age"));
            tboxPhone.setText(getIntent().getStringExtra("customer_phone"));
            tboxAL1.setText(getIntent().getStringExtra("customer_AL1"));
            tboxAL2.setText(getIntent().getStringExtra("customer_AL2"));
            tboxFname.setEnabled(false);
            tboxMname.setEnabled(false);
            tboxLname.setEnabled(false);
            tboxGender.setAdapter(null);
            tboxAge.setEnabled(false);
            tboxPhone.setEnabled(false);
            tboxAL1.setEnabled(false);
            tboxAL2.setEnabled(false);
        }

        final Calendar calendar = Calendar.getInstance();

        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        datePicker = new DatePickerDialog(AppointmentActivity.this);

        tboxDateTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                    datePicker = new DatePickerDialog(AppointmentActivity.this, (view, year1, month1, dayOfMonth) -> {
                    tboxDateTime.setText(year1 + "-" + (month1 + 1) + "-" + dayOfMonth);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                    Date date = new Date(year1, month1, dayOfMonth-1);
                    restrictedDay = simpledateformat.format(date);
                    cardTime.setVisibility(View.VISIBLE);
                    getAvail(clinic_id);
                }, year, month, day);

                datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePicker.show();
            }
        });

        tboxTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(AppointmentActivity.this, (view, hourOfDay, minute1) -> {
                tboxTime.setText(hourOfDay + ":" + "00");
                Log.d("TAG", "onClick: " + tboxTime.getText());
                getTimeDay(clinic_id);
            }, hour, minute, false);

            timePickerDialog.show();
            }
        });

        sprPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                packages_id = String.valueOf(Integer.parseInt(packagesArrayList.get(i).getId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                packages_id = String.valueOf(Integer.parseInt(packagesArrayList.get(0).getId()));
            }
        });

        selectServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServicesChooser servicesChooser = new ServicesChooser();
                Bundle bundle = new Bundle();
                bundle.putString("clinic_id", clinic_id);
                Log.d("TAG", "onClick: " + clinic_id);
                servicesChooser.setArguments(bundle);
                servicesChooser.show(getSupportFragmentManager(), "CHOOSE SERVICES");
            }
        });

        GetPackages(clinic_id);

        btnAppSubmit.setOnClickListener(v -> {
            String a1, a2, a3, a4, a5, a6, a8, a9, a10;

            a1 = tboxFname.getText().toString();
            a2 = tboxMname.getText().toString();
            a3 = tboxLname.getText().toString();
            a4 = tboxAge.getText().toString();
            a5 = tboxPhone.getText().toString();
            a6 = tboxAL1.getText().toString();
            a8 = tboxGender.getText().toString();
            a9 = tboxDateTime.getText().toString();
            a10 = tboxTime.getText().toString();

            boolean checker = appointmentValidator(a1, a2, a3, a4, a5, a6, a8, a9, a10);

            if (checker){
                if (!SERVICE_IDS.equals("0") || !packages_id.equals("0")){
                    Toast.makeText(this, "Appointment is Set", Toast.LENGTH_SHORT).show();
                    storeAppointment();
                } else
                {
                    Toast.makeText(this, "Please choose at least 1 service or package.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please check information again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTimeDay(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getApplicationContext().getString(R.string.URLProject) + "api/mcustomer/mappointment/" + id,
                null,
                response -> {
                    try {
                        JSONArray jsonAvail = response.getJSONArray("avail");
                        for (int i = 0; i < jsonAvail.length(); i++) {
                            String availStatus = "Close";
                            String availMin_time = "", availMax_time = "";

                            JSONObject jsonObject = jsonAvail.getJSONObject(i);

                            String availDay = jsonObject.getString("day");
                            availMin_time = jsonObject.getString("min");
                            availMax_time = jsonObject.getString("max");
                            if (jsonObject.getString("status").equals("on")){
                                availStatus = "Open";
                            }

                            String pattern = "HH:MM";
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                            Date timeMin = sdf.parse(availMin_time);
                            Date timeMax = sdf.parse(availMax_time);

                            Date timeCurr = sdf.parse(String.valueOf(tboxTime.getText()));

                            Log.d("TAG", "getTimeDay: "+timeMin + " " + timeMax + timeCurr);


                            String theDay = "id"+availDay;

                            if (theDay.equals("idSunday")){
                                if(timeMin.after(timeCurr) || timeMax.before(timeCurr) || timeMax.equals(timeCurr)){
                                    Log.d("TAG", "getTimeDay: ASDKLJASLKDJLASJDASJDJASD");
                                    Toast.makeText(this, "This day is only available between " + availMin_time + " to " + availMax_time, Toast.LENGTH_SHORT).show();
                                    tboxTime.setText("");
                                }
//                                idSunday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
//                                Log.d("TAG", "getAvail: " + idSunday);
//

//                                if ((Integer.parseInt(availMin_time) > Integer.parseInt(String.valueOf(tboxTime))) && (Integer.parseInt(availMax_time) < Integer.parseInt(String.valueOf(tboxTime)))){
//                                    Toast.makeText(this, "This day is only available from " + availMin_time + " to " + availMax_time, Toast.LENGTH_SHORT).show();
//                                }

                            }

                            if (theDay.equals("idMonday")){
                                idMonday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idMonday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idTuesday")){
                                idTuesday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idTuesday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idWednesday")){
                                idWednesday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idWednesday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idThursday")){
                                idThursday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idThursday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idFriday")){
                                idFriday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idFriday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idSaturday")){
                                idSaturday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idSaturday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("VolleyError", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getAvail(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getApplicationContext().getString(R.string.URLProject) + "api/mcustomer/mappointment/" + id,
                null,
                response -> {
                    try {
                        JSONArray jsonAvail = response.getJSONArray("avail");
                        for (int i = 0; i < jsonAvail.length(); i++) {
                            String availStatus = "Close";
                            String availMin_time = "", availMax_time = "";

                            JSONObject jsonObject = jsonAvail.getJSONObject(i);

                            String availDay = jsonObject.getString("day");
                            availMin_time = jsonObject.getString("min");
                            availMax_time = jsonObject.getString("max");
                            if (jsonObject.getString("status").equals("on")){
                                availStatus = "Open";
                            }

                            String theDay = "id"+availDay;

                            if (theDay.equals("idSunday")){
                                idSunday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idSunday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idMonday")){
                                idMonday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idMonday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idTuesday")){
                                idTuesday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idTuesday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idWednesday")){
                                idWednesday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idWednesday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idThursday")){
                                idThursday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idThursday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idFriday")){
                                idFriday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idFriday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }

                            if (theDay.equals("idSaturday")){
                                idSaturday = (availDay  + ": " + availMin_time + " - " + availMax_time + " Status: " + availStatus);
                                Log.d("TAG", "getAvail: " + idSaturday);
                                if (restrictedDay.equals(availDay) && availStatus.equals("Close")){
                                    Toast.makeText(this, "Sorry, we are close for the Day Selected", Toast.LENGTH_SHORT).show();
                                    tboxDateTime.setText("");
                                    cardTime.setVisibility(View.GONE);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("VolleyError", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private boolean appointmentValidator(String a1, String a2, String a3, String a4, String a5, String a6, String a8, String a9, String a10) {
        if (a1.isEmpty()) {
            tboxFname.requestFocus();
            tboxFname.setError("First Name can't be empty");
            return false;
        } else if (!a1.matches("[^0-9\\.\\,\\\"\\?\\!\\;\\:\\#\\$\\%\\&\\(\\)\\*\\+\\-\\/\\<\\>\\=\\@\\[\\]\\\\\\^\\_\\{\\}\\|\\~]+")){
            tboxFname.requestFocus();
            tboxFname.setError("Please set a valid First Name");
            return false;
        }

        if (!a2.isEmpty()){
            if (!a2.matches("[^0-9\\.\\,\\\"\\?\\!\\;\\:\\#\\$\\%\\&\\(\\)\\*\\+\\-\\/\\<\\>\\=\\@\\[\\]\\\\\\^\\_\\{\\}\\|\\~]+")){
                tboxMname.requestFocus();
                tboxMname.setError("Please set a valid Middle Name");
                return false;
            }
        }

        if (a3.isEmpty()){
            tboxLname.requestFocus();
            tboxLname.setError("Last Name can't be empty");
            return false;
        } else if (!a3.matches("[^0-9\\.\\,\\\"\\?\\!\\;\\:\\#\\$\\%\\&\\(\\)\\*\\+\\-\\/\\<\\>\\=\\@\\[\\]\\\\\\^\\_\\{\\}\\|\\~]+")){
            tboxLname.requestFocus();
            tboxLname.setError("Please set a valid Last Name");
            return false;
        }

        if (a4.isEmpty()) {
            tboxAge.requestFocus();
            tboxAge.setError("Age can't be Empty");
            return false;
        } else if(Integer.parseInt(a4) <= 0 || Integer.parseInt(a4) >= 120) {
            tboxAge.requestFocus();
            tboxAge.setError("Please enter a valid age");
            return false;
        }

        if (a5.isEmpty()){
            tboxPhone.requestFocus();
            tboxPhone.setError("Contact Number can't be Empty");
            return false;
        } else if(!a5.matches("(09)\\d{9}")){
            tboxPhone.requestFocus();
            tboxPhone.setError("Must be a valid or 11 - digits number");
            return false;
        }

        if(a6.isEmpty()){
            tboxAL1.requestFocus();
            tboxAL1.setError("Address Line 1 can't be Empty");
            return false;
        }

        if (a8.isEmpty()) {
            tboxGender.requestFocus();
            tboxGender.setError("Gender can't be Empty.");
            return false;
        }

        if (a9.isEmpty()){
            tboxDateTime.requestFocus();
            tboxDateTime.setError("Please Select Date for your Appointment");
            return false;
        }

        if (a10.isEmpty()){
            tboxTime.requestFocus();
            tboxTime.setError("Please Select Time for your Appointment");
            return false;
        }
        return true;
    }

    @Override
    public void services_name(String name) {
        textServices.setText(name);
    }

    @Override
    public void services_id(String id) {
        SERVICE_IDS = id;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
        @SuppressLint("NewApi")
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == cBoxPackages) {
                if (!isChecked) {
                    Log.d("CheckBox1", "Unchecked");
                    packages_id = "0";
                    cardPackage.setVisibility(View.GONE);
                } else {
                    Log.d("CheckBox1", "Checked");
                    cardPackage.setVisibility(View.VISIBLE);
                }
            }
            if (buttonView == cBoxServices) {
                if (!isChecked) {
                    Log.d("CheckBox2", "Unchecked");
                    SERVICE_IDS = "0";
                    textServices.setText("");
                    selectServices.setVisibility(View.GONE);
                } else {
                    Log.d("CheckBox2", "Checked");
                    selectServices.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void GetPackages(String id){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URLProjectwithhttp) + "api/mcustomer/mappointment/" + id + "/edit/",

                null,
                response -> {
                    try {
                        Log.d("RESPONSE", response.toString());
                        JSONArray jsonPackage = response.getJSONArray("package");
                        for (int i = 0; i < jsonPackage.length(); i++) {
                            JSONObject jsonObject = jsonPackage.getJSONObject(i);
                            Packages packages = new Packages(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("name")
                            );
                            packagesArrayList.add(packages);
                            stringPackage.add(packages.getName());
//                            packagesArrayList.set(i, packages);
                        }
                        packageArray = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, stringPackage);
                        packageArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sprPackage.setAdapter(packageArray);
                    }catch (JSONException e){
                        Log.d("ERROR", e.toString());
                    }
                },
                error -> Log.e("volleyError", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SessionClass.FRAGMENT = new MailAppointmentFragment();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void storeAppointment() {
        String checkService = SERVICE_IDS;
        String checkPackage = packages_id;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_as_customer_id", user_id);
            jsonObject.put("fname", tboxFname.getText().toString());
            jsonObject.put("mname", tboxMname.getText().toString());
            jsonObject.put("lname", tboxLname.getText().toString());
            jsonObject.put("gender", tboxGender.getText().toString());
            jsonObject.put("age", tboxAge.getText().toString());
            jsonObject.put("phone", tboxPhone.getText().toString());
            jsonObject.put("addline1", tboxAL1.getText().toString());
            jsonObject.put("addline2", tboxAL2.getText().toString());
            jsonObject.put("clinic_id", clinic_id);
            if (!checkService.equals("0")){
                jsonObject.put("service_ids", SERVICE_IDS.replace("[", "").replace("]", ""));
            }
            if (!checkPackage.equals("0")){
                jsonObject.put("package", packages_id);
            }
            jsonObject.put("date", tboxDate.getText().toString());

            jsonObject.put("datetime", tboxDateTime.getText().toString() + " " + tboxTime.getText().toString());
            Log.d("TAG", "storeAppointment: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.URLProject) + "api/mcustomer/mappointment";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            Log.d("RESPONSE", "onResponse: " + response.toString());
                            Log.d("RESPONSE", "onResponse: " + response.getString("message"));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volleyError", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}
