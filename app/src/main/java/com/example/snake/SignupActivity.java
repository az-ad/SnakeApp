package com.example.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity    {

    private EditText datepickerEditText;
    private EditText signUpEmailEditText,signUpPasswordEditText,signUpUserNameEditText,signUpPhoneNumber;
    private TextView logInTextView;
    private Button signUpButton;
    private Spinner facultySpinner,departmentSpinner;
    private TextView facultyTextView,departmentTextView;
    private  String selectedFaculty,selectedDepartment;
    private ArrayAdapter<CharSequence> facultyAdapter, departmentAdapter;

    private DatePickerDialog datePiker;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser muser  = mAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databaseReference = FirebaseDatabase.getInstance().getReference("SignUp");
        progressBar = findViewById(R.id.progressbarId);
        mAuth = FirebaseAuth.getInstance();

        signUpButton=findViewById(R.id.signUpButton_Id);
        datepickerEditText=findViewById(R.id.datepickerEditText_Id);
        signUpUserNameEditText=findViewById(R.id.userNameEditText_Id);
        signUpPhoneNumber=findViewById(R.id.phoneNumberEditText_Id);
        signUpEmailEditText=findViewById(R.id.emailEditText_Id);
        signUpPasswordEditText=findViewById(R.id.passwordEditText_Id);
        logInTextView=findViewById(R.id.logInTextView_Id);
        facultySpinner =findViewById(R.id.facultySpinner_Id);
        departmentSpinner=findViewById(R.id.departmentSpinner_Id);
        facultyTextView= findViewById(R.id.facultyTextView_Id);
        departmentTextView=findViewById(R.id.departmentTextView_Id);

//--------------------multiple dropDown list---------------------------//
        //Populate ArrayAdapter using string array and a spinner layout that we will define
        facultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_faculty, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appear
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Set the adapter to the spinner to populate the State Spinner
        facultySpinner.setAdapter(facultyAdapter);
        //When any item of the stateSpinner uis selected
        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Define City Spinner but we will populate the options through the selected Faculty
                departmentSpinner = findViewById(R.id.departmentSpinner_Id);
                //Obtain the selected Faculty
                selectedFaculty = facultySpinner.getSelectedItem().toString();

                int parentID = parent.getId();
                if (parentID == R.id.facultySpinner_Id) {
                    switch (selectedFaculty) {
                        case "Select Your Faculty":
                            departmentAdapter
                                    = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_default_departments, R.layout.spinner_layout);
                            break;
                        case "Engineering": departmentAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_engineering_departments, R.layout.spinner_layout);
                            break;
                        case "Arts": departmentAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_arts_departments, R.layout.spinner_layout);
                            break;
                        case "Science": departmentAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_science_departments, R.layout.spinner_layout);
                            break;
                        case "Business Administration": departmentAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_business_administration_departments, R.layout.spinner_layout);
                            break;
                        case "Social Science": departmentAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_social_science_departments, R.layout.spinner_layout);
                            break;
                        default:break;
                    }
                    // Specify the layout to use when the list of choices appears
                    departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Populate the list of Districts in respect of the State selected
                    departmentSpinner.setAdapter( departmentAdapter);

                    //To obtain the selected District from the spinner
                    departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedDepartment = departmentSpinner.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        \\------------------------check User Name------------------------------------\\
        signUpUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String typeName = String.valueOf(s);

                databaseReference.orderByChild("userName").equalTo(typeName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            signUpUserNameEditText.setError("Username is not available");
                            signUpUserNameEditText.requestFocus();
                        }

//                        else {
//                           // Toast.makeText(getApplicationContext(),"Username is available",Toast.LENGTH_SHORT).show();
//                           // signUpUserNameEditText.setError("Username is available");
//                            //signUpUserNameEditText.setText("Username is available");
//                            signUpUserNameEditText.requestFocus();
//
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
 //        \\------------------------check Email pattern------------------------------------\\
        signUpEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String Email = String.valueOf(s);
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches())
                {
                    signUpEmailEditText.setError("Enter a valid email address");
                    signUpEmailEditText.requestFocus();
                    return;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        \\------------------------check Password pattern------------------------------------\\
        signUpPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<6)
                {
                    signUpPasswordEditText.setError("Minimum length of a password should be 6");
                    signUpPasswordEditText.requestFocus();
                    return;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



//------------datePicker for calender-------------------------------//
        datepickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month+1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        datepickerEditText.setText(date);

                    }
                },year, month,day);
                dialog.show();

            }
        });

        //signUpButton.setOnClickListener((View.OnClickListener) this);
        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();

            }
        });



    }//end oncreate




    private void userRegister() {
        String email= signUpEmailEditText.getText().toString().trim();
        String password= signUpPasswordEditText.getText().toString().trim();
        String userName= signUpUserNameEditText.getText().toString().trim();
        String phoneNumber=signUpPhoneNumber.getText().toString().trim();
        String dateOfBirth = datepickerEditText.getText().toString().trim();

        selectedFaculty = facultySpinner.getSelectedItem().toString();
        selectedDepartment = departmentSpinner.getSelectedItem().toString();

        progressBar.setVisibility(View.VISIBLE);

        //checking the validity of the username
        if(userName.isEmpty())
        {
            signUpUserNameEditText.setError("Enter a unique user name");
            signUpUserNameEditText.requestFocus();
            return;
        }
        //checking the validity of the phone number
        if(phoneNumber.isEmpty())
        {
            signUpPhoneNumber.setError("Enter a valid phone number");
            signUpPhoneNumber.requestFocus();
            return;
        }
//checking the validity of the date of birth
        if(dateOfBirth.isEmpty())
        {
            datepickerEditText.setError("Enter a valid birth date");
            datepickerEditText.requestFocus();
            return;
        }

        //checking the validity of the Email
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signUpEmailEditText.setError("Enter a valid email address");
            signUpEmailEditText.requestFocus();
            return;
        }
        //checking the validity of the password
        if(password.isEmpty())
        {
            signUpPasswordEditText.setError("Enter a password");
            signUpPasswordEditText.requestFocus();
            return;
        }

//-------------------------check the faculty & department either empty or not--------------------------------------//
        if (selectedFaculty.equals("Select Your Faculty")) {
            Toast.makeText(SignupActivity.this, "Please select your Faculty from the list", Toast.LENGTH_LONG).show();
            facultyTextView.setError("Faculty is required!");      //To set error on TextView
            facultyTextView.requestFocus();
            return;
        } else if (selectedDepartment.equals("Select Your Department")) {
            Toast.makeText(SignupActivity.this, "Please select your department from the list", Toast.LENGTH_LONG).show();
            departmentTextView.setError("Department is required!");
            departmentTextView.requestFocus();
            facultyTextView.setError(null);
            return;
        } else {
            facultyTextView.setError(null);
            departmentTextView.setError(null);
            Toast.makeText(SignupActivity.this, "Selected Faculty: "+selectedFaculty+"\nSelected District: "+selectedDepartment, Toast.LENGTH_LONG).show();
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            
                            saveData();

                            finish();
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(getApplicationContext(),"User is already registered",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });




    }

    public void saveData() {
//        String comment = commentEditText.getText().toString().trim();
//        String rating = String.valueOf(rateRatingBar.getRating());

        String email= signUpEmailEditText.getText().toString().trim();
        String password= signUpPasswordEditText.getText().toString().trim();
        String userName= signUpUserNameEditText.getText().toString().trim();
        String phoneNumber=signUpPhoneNumber.getText().toString().trim();
        String dateOfBirth = datepickerEditText.getText().toString().trim();

        String faculty=facultySpinner.getSelectedItem().toString();
        String department=departmentSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(userName)) {
            // if the text fields are empty
            // then show the below message.
            Toast.makeText(SignupActivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();
        }
        else {
            String key = databaseReference.push().getKey();
            SignUpInfo signUpInfo = new SignUpInfo(email,password,userName,phoneNumber,dateOfBirth,faculty,department);
            databaseReference.child(muser.getUid()).setValue(signUpInfo);
            //UserComment userComment = new UserComment(comment,rating);
            //databaseReference.child(key).setValue(userComment);
            //Toast.makeText(RatingActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
//            commentEditText.setText("");
//            float v = 0;
//            rateRatingBar.setRating(v);
        }

    }

}

