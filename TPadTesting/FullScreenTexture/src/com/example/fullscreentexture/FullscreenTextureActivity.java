/*
 * Copyright 2014 TPad Tablet Project. All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ARSHAN POURSOHI OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied.
 */

package com.example.fullscreentexture;

import java.math.BigDecimal;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;

import nxr.tpad.lib.TPad;
import nxr.tpad.lib.TPadImpl;
import nxr.tpad.lib.views.FrictionMapView;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.fullscreentexture.R;

public class FullscreenTextureActivity extends Activity {

	// Custom Haptic Rendering view defined in TPadLib
	FrictionMapView fricView;
	
	// TPad object defined in TPadLib
	TPad mTpad;
	
	
	
	
	////////////////////////from paypal
	
	 private static final String TAG = "paymentExample";
	    /**
	     * - Set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
	     * 
	     * - Set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials
	     * from https://developer.paypal.com
	     * 
	     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
	     * without communicating to PayPal's servers.
	     */
	    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

	    // note that these credentials will differ between live & sandbox environments.
	    private static final String CONFIG_CLIENT_ID = "credential from developer.paypal.com";

	    private static final int REQUEST_CODE_PAYMENT = 1;
	    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

	    private static PayPalConfiguration config = new PayPalConfiguration()
	            .environment(CONFIG_ENVIRONMENT)
	            .clientId(CONFIG_CLIENT_ID)
	            // The following are only used in PayPalFuturePaymentActivity.
	            .merchantName("Hipster Store")
	            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
	            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
	
	//////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);
		
		// Set the content of the screen to the .xml file that is in the layout folder
		setContentView(R.layout.activity_hello_tpad);
		
		// Load new tpad object from TPad Implementation Library
		mTpad = new TPadImpl(this);
		
		// Link friction view to .xml file
		fricView = (FrictionMapView) findViewById(R.id.view1);
		
		// Link local tpad object to the FrictionMapView
		fricView.setTpad(mTpad);
		
		// Load in the image stored in the drawables folder
		Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.morph);
		
		// Set the friction data bitmap to the test image
		fricView.setDataBitmap(defaultBitmap);
	
	}
	
	public void onBuyPressed(View pressed){
		
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(FullscreenTextureActivity.this, PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        
        
	}
	
	private PayPalPayment getThingToBuy(String paymentIntent) {
		return new PayPalPayment(new BigDecimal("1.75"), "USD",
				"hipster jeans", paymentIntent);
	}

	@Override
	protected void onDestroy() {
		mTpad.disconnectTPad();
		super.onDestroy();
	}
	

}