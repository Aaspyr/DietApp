package com.example.godiet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    /*Variables */
    private String[] arraySpinnerDOBDay = new String[31];
    private String[] arraySpinnerDOBYear = new String[100];


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        /* Fill numbers for date of birth days*/
        int human_counter = 0;
        for(int x=0;x<31;x++){
            human_counter=x+1;
            this.arraySpinnerDOBDay [x] = "" +human_counter;
        }

        Spinner spinnerDOBDay = (Spinner) findViewById(R.id.spinnerDOBDay);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerDOBDay);
        spinnerDOBDay.setAdapter(adapter);

        /* Fill numbers for date of birth year */
        //get current year, month and data
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int end = year-100;
        int index = 0;
        for(int x=year;x>end;x--){
            this.arraySpinnerDOBYear[index] = "" + x;
            index++;
        }

        Spinner spinnerDOBYear = (Spinner) findViewById(R.id.spinnerDOBYear);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerDOBYear);
        spinnerDOBYear.setAdapter(adapterYear);



        /*Hide error icon and message */
        ImageView imageViewError = (ImageView)findViewById(R.id.imageViewError);
        imageViewError.setVisibility(View.GONE);

        TextView textViewErrorMessage = (TextView)findViewById(R.id.textViewErrorMessage);
        textViewErrorMessage.setVisibility(View.GONE);

        /* Hide inches field */
        EditText editTextHeightInches = (EditText)findViewById(R.id.editTextHeightInches);
        editTextHeightInches.setVisibility(View.GONE);


        /* Listener Mesurment spinner */
        Spinner spinnerMeasurement  = (Spinner)findViewById(R.id.spinnerMeasurement );
        spinnerMeasurement .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                measurementChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // measurementChanged();
            }
        });



        /* Listener buttonSignUp */
        Button buttonSignUp = (Button)findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signUpSubmit();
            }
        });

    }//protected void OnCreate

    /*- Measurement Changed ---------------------------------------------- */
    public void measurementChanged(){

        // Measurement spinner
        Spinner spinnerMeasurement = (Spinner)findViewById(R.id.spinnerMeasurement);
        String stringMeasurement = spinnerMeasurement.getSelectedItem().toString();

        EditText editTextHeightCm = (EditText)findViewById(R.id.editTextHeightCm);
        EditText editTextHeightInches = (EditText)findViewById(R.id.editTextHeightInches);
        String stringHeightCm = editTextHeightCm.getText().toString();
        String stringHeightInches = editTextHeightInches.getText().toString();

        double heightCm = 0;
        double heightFeet = 0;
        double heightInches = 0;

        TextView textViewCm = (TextView)findViewById(R.id.textViewCm);
        TextView textViewKg = (TextView)findViewById(R.id.textViewKg);

        if (stringMeasurement.startsWith("I")){
            //Metric
            editTextHeightInches.setVisibility(View.VISIBLE);
            textViewCm.setText("feet and inches");
            textViewKg.setText("pound");

            // Find feet
            try {
                heightCm = Double.parseDouble(stringHeightCm);
            }
            catch (NumberFormatException nfe){

            }

            if(heightCm != 0){
                // Convert CM to feet
                // feet = (cm * 0.3937008)/12
                heightFeet = (heightCm * 0.3937008)/12;
                int intHeightFeet = (int)heightFeet;

                editTextHeightCm.setText("" + intHeightFeet);
            }
        }
        else {
            //Imperial
            editTextHeightInches.setVisibility(View.GONE);
            textViewCm.setText("cm");
            textViewKg.setText("kg");

            // Change feet and inches to cm

            // Convert Feet
            try{
                heightFeet = Double.parseDouble(stringHeightCm);
            }
            catch (NumberFormatException nfe){

            }

            // Convert inches
            try{
                heightInches = Double.parseDouble(stringHeightInches);
            }
            catch (NumberFormatException nfe){

            }

            // Need to convert, we want to save the number in cm
            // cm = ((foot * 12) + inches) * 2.54
            if(heightFeet != 0 && heightInches != 0){
                heightCm = ((heightFeet * 12) + heightInches) * 2.54;
                heightCm = Math.round(heightCm);
                editTextHeightCm.setText("" + heightCm);
            }
        }

        // Weight
        EditText editTextWeight = (EditText)findViewById(R.id.editTextWeight);
        String stringWeight = editTextWeight.getText().toString();
        double doubleWeight = 0;

        try{
            doubleWeight = Double.parseDouble(stringWeight);
        }
        catch (NumberFormatException nfe){

        }

        if(doubleWeight != 0){
            if(stringMeasurement.startsWith("I")){
                // kg to pounds
                doubleWeight = Math.round(doubleWeight/0.45359237);
            } else{
                // pounds to kg
                doubleWeight = Math.round(doubleWeight*0.45359237);
            }
            editTextWeight.setText("" + doubleWeight);
        }

    } // public void measurementChanged

    /*- Sign up Submit ---------------------------------------------- */
    public void signUpSubmit() {
        //Error
        ImageView imageViewError = (ImageView)findViewById(R.id.imageViewError);
        TextView textViewErrorMessage = (TextView)findViewById(R.id.textViewErrorMessage);
        String errorMessage = "";

        // email
        TextView textView = (TextView)findViewById(R.id.textView);
        EditText editTextTextEmailAddress = (EditText)findViewById(R.id.editTextTextEmailAddress);
        String stringEmail = editTextTextEmailAddress.getText().toString();
        if(stringEmail.isEmpty()|| stringEmail.startsWith(" ")){
            textView.setTextColor(Color.RED);
            errorMessage = "Please fill in an e-mail address";
        }
        else{
            textView.setTextColor(Color.GRAY);
        }
        //Date of Birth Day
        Spinner spinnerDOBDay = (Spinner)findViewById(R.id.spinnerDOBDay);
        String stringDOBDay = spinnerDOBDay.getSelectedItem().toString();
        int intDOBDay = 0;
        try {
            intDOBDay = Integer.parseInt(stringDOBDay);
            if(intDOBDay < 10){
                stringDOBDay = "0" + stringDOBDay;
            }
        }
        catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
            errorMessage = "Please select a day for your birthday.";
        }

        // Date of Birth Month
        Spinner spinnerDOBMonth = (Spinner)findViewById(R.id.spinnerDOBMonth);
        String stringDOBMonth = spinnerDOBMonth.getSelectedItem().toString();
        if(stringDOBMonth.startsWith("Jan")){
            stringDOBMonth = "01";
        }
        else if(stringDOBMonth.startsWith("Feb")){
            stringDOBMonth = "02";
        }
        else if(stringDOBMonth.startsWith("Mar")){
            stringDOBMonth = "03";
        }
        else if(stringDOBMonth.startsWith("Apr")){
            stringDOBMonth = "04";
        }
        else if(stringDOBMonth.startsWith("May")){
            stringDOBMonth = "05";
        }
        else if(stringDOBMonth.startsWith("Jun")){
            stringDOBMonth = "06";
        }
        else if(stringDOBMonth.startsWith("Jul")){
            stringDOBMonth = "07";
        }
        else if(stringDOBMonth.startsWith("Aug")){
            stringDOBMonth = "08";
        }
        else if(stringDOBMonth.startsWith("Sep")){
            stringDOBMonth = "09";
        }
        else if(stringDOBMonth.startsWith("Oct")){
            stringDOBMonth = "10";
        }
        else if(stringDOBMonth.startsWith("Nov")){
            stringDOBMonth = "11";
        }
        else if(stringDOBMonth.startsWith("Dec")){
            stringDOBMonth = "12";
        }

        // Date of Birth Year
        Spinner spinnerDOBYear = (Spinner)findViewById(R.id.spinnerDOBYear);
        String stringDOBYear = spinnerDOBYear.getSelectedItem().toString();
        int intDOBYear = 0;
        try {
            intDOBYear = Integer.parseInt(stringDOBYear);
        }
        catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
            errorMessage = "Please select a year for your birthday.";
        }

        // Put date of birth togheter
        String dateOfBirth = intDOBYear + "-" + stringDOBMonth + "-" + stringDOBDay;

        // Gender
        RadioGroup radioGroupGender = (RadioGroup)findViewById(R.id.radioGroupGender);
        int radioButtonId = radioGroupGender.getCheckedRadioButtonId(); // get selected radio button from radioGroup
        View radioButtonGender = radioGroupGender.findViewById(radioButtonId);
        int position = radioGroupGender.indexOfChild(radioButtonGender);
        //RadioButton radioButtonGender = (RadioButton) findViewById(selectedId); // find the radiobutton by returned id
        String stringGender = "";
        if(position == 0){
            stringGender = "male";
        } else{
            stringGender = "female";
        }

        // Height
        EditText editTextHeightCm = (EditText)findViewById(R.id.editTextHeightCm);
        EditText editTextHeightInches = (EditText)findViewById(R.id.editTextHeightInches);
        String stringHeightCm = editTextHeightCm.getText().toString();
        String stringHeightInches = editTextHeightInches.getText().toString();

        double heightCm = 0;
        double heightFeet = 0;
        double heightInches = 0;
        boolean metric = true;

        // Metric or imperial?
        Spinner spinnerMeasurement = (Spinner)findViewById(R.id.spinnerMeasurement);
        String stringMeasurement = spinnerMeasurement.getSelectedItem().toString();

        int intMesurment = spinnerMeasurement.getSelectedItemPosition();
        if(intMesurment == 0){
            stringMeasurement = "metric";
        } else{
            stringMeasurement = "imperial";
            metric = false;
        }

        if(metric == true){

            // Convert CM
            try{
                heightCm = Double.parseDouble(stringHeightCm);
                heightCm = Math.round(heightCm);
            }
            catch(NumberFormatException nfe){
                errorMessage = "Height (cm) has to be a number";
            }
        } else{
            // Convert Feet
            try{
                heightFeet = Double.parseDouble(stringHeightCm);
            }
            catch(NumberFormatException nfe){
                errorMessage = "Height (feet) has to be a number";
            }

            // Convert inches
            try{
                heightInches = Double.parseDouble(stringHeightInches);
            }
            catch(NumberFormatException nfe){
                errorMessage = "Height (inches) has to be a number";
            }

            // Feet and inches
            // Need to convert, we want to save the number in cm
            // cm = ((foot * 12) + inches) * 2.54
            heightCm = ((heightFeet * 12) + heightInches) * 2.54;
            heightCm = Math.round(heightCm);
        }

        // Weight
        EditText editTextWeight = (EditText)findViewById(R.id.editTextWeight);
        String stringWeight = editTextWeight.getText().toString();
        double doubleWeight = 0;

        try{
            doubleWeight = Double.parseDouble(stringWeight);
        }
        catch(NumberFormatException nfe){
            errorMessage = "Weight has to be a number";
        }

        if(metric == true){

        } else{
            // Imperial
            // Pound to kg
            doubleWeight = Math.round(doubleWeight * 0.45359237);
        }

        // Activity level
        Spinner spinnerActivityLevel = (Spinner)findViewById(R.id.spinnerActivityLevel);
        // 0: Little to no exercise
        // 1: Light exercise
        // 2: Moderate exercise
        // 3: Heavy exercise
        // 4: Very heavy exercise
        int intActivityLevel = spinnerActivityLevel.getSelectedItemPosition();

        //Error handling
        if(errorMessage.isEmpty()){
            //Put data into database
            imageViewError.setVisibility(View.GONE);
            textViewErrorMessage.setVisibility(View.GONE);

            // Insert into database
            DBAdapter db = new DBAdapter(this);
            db.open();

            // Quote smart
            String stringEmailSQL = db.quoteSmart(stringEmail);
            String dateOfBirthSQL = db.quoteSmart(dateOfBirth);
            String stringGenderSQL = db.quoteSmart(stringGender);
            double heightCmSQL = db.quoteSmart(heightCm);
            int intActivityLevelSQL = db.quoteSmart(intActivityLevel);
            double doubleWeightSQL = db.quoteSmart(doubleWeight);
            String stringMesurmentSQL = db.quoteSmart(stringMeasurement);

            //Input for users
            String stringInput = "NULL, " + stringEmailSQL + "," + dateOfBirthSQL + "," + stringGenderSQL + "," + heightCmSQL + "," + intActivityLevelSQL + "," + stringMesurmentSQL;
            db.insert("users",
                    "user_id, user_email, user_dob, user_gender, user_height, user_activity_level, user_mesurment", stringInput);

            //Input for goal
            DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
            String goalDate = df1.format(Calendar.getInstance().getTime());

//            Calendar cc = Calendar.getInstance();
//            int year = cc.get(Calendar.YEAR);
//            int month = cc.get(Calendar.MONTH);
//            int mDay = cc.get(Calendar.DAY_OF_MONTH);
//            String goalDate = year + "-" + month + "-" + mDay;
            String goalDateSQL = db.quoteSmart(goalDate);

            stringInput = "NULL, " + doubleWeightSQL + "," + goalDateSQL;
            db.insert("goal",
                    "goal_id, goal_current_weight, goal_date",
                    stringInput);
            db.close();

            // Move user back to MainActivity
            Intent i = new Intent(SignUp.this, SignUpGoal.class);
            startActivity(i);

        }else {
            // There is error
            textViewErrorMessage.setText(errorMessage);
            imageViewError.setVisibility(View.VISIBLE);
            textViewErrorMessage.setVisibility(View.VISIBLE);
        }
    }
} //public class SignUp