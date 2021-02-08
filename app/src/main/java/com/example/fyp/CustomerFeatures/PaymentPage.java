package com.example.fyp.CustomerFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fyp.R;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.param.AccountCreateParams;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymentPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        Stripe.apiKey = "sk_test_51IFKQvKgTR7yUeIexy2I5Th0pv3OGDiM088vBZY2YFHLkJO1uxZrCesiQoGzUewSLdkwnkcETWhlwk5bGlUCsrLB00FF8KPznk";

        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setType(AccountCreateParams.Type.STANDARD)
                        .build();

        try {
            Account account = Account.create(params);
        } catch ( StripeException e) {
            e.printStackTrace();
        }
    }
}