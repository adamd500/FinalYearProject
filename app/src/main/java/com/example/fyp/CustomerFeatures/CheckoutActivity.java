package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.ProfessionalFeatures.SelectedNewJob;
import com.example.fyp.R;
import com.google.android.gms.common.api.Response;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntent;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.security.auth.callback.Callback;

public class CheckoutActivity extends AppCompatActivity {

    Stripe stripe;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    String clientSecret,jobId;
    TextView t1;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        Intent intent1 = getIntent();
        clientSecret = intent1.getStringExtra("clientSecret");
        jobId = intent1.getStringExtra("jobId");
        price=intent1.getStringExtra("price");

        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51IFKQvKgTR7yUeIeGWCvBXHkv6Tp5hY4BPW2PgeIbWCkAKNjsvxnFLbS0vbOCxMSkB0fc36bK25XQD9ZloDoBoOC00YTntPZOh"
        );

        t1=(TextView)findViewById(R.id.txtViewTrade);
        t1.setText(price);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, clientSecret);

                final Context context = getApplicationContext();
                stripe = new Stripe(
                        context,
                        PaymentConfiguration.getInstance(context).getPublishableKey()
                );
                stripe.confirmPayment(this, confirmParams);
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));

        Toast.makeText(this, "Payment Processed. Thank You", Toast.LENGTH_SHORT).show();
        ref.child("Job").child(jobId).child("finished").setValue(true);
        Intent intent = new Intent(this,PaymentSuccess.class);
        startActivity(intent);

    }

    // ...

    private static final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<CheckoutActivity> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
//
            com.stripe.android.model.PaymentIntent paymentIntent =result.getIntent();
           // Intent intent = new Intent(this,WelcomeCustomer.class);

//          PaymentIntent paymentIntent = result.getIntent();
           // PaymentIntent status = paymentIntent.getStatus();
           // if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
           //     Gson gson = new GsonBuilder().setPrettyPrinting().create();
     //           activity.displayAlert(
//                        "Payment completed",
//                        gson.toJson(paymentIntent),
//                        true
//                );
//            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
//                // Payment failed
//                activity.displayAlert(
//                        "Payment failed",
//                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage(),
//                        false
//                );
//            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            // Payment request failed – allow retrying using the same payment method
          //  activity.displayAlert("Error", e.toString(), false);
           // activity.
        }
    }


    //

}

