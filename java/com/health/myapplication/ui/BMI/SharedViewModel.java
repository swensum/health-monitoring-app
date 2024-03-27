package com.health.myapplication.ui.BMI;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> bmiResult = new MutableLiveData<>();

    public LiveData<String> getBmiResult() {
        return bmiResult;
    }

    public void setBmiResult(String bmiResult) {
        this.bmiResult.setValue(bmiResult);
    }
}

