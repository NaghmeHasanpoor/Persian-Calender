package com.arpitas.persiancalender.connections;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class IpChecker extends StringRequest {

   private static final String url = "http://116.203.75.196/api/is_valid";

   private IPCheckerListener ipCheckerListener = null;

   public IpChecker(IPCheckerListener ipCheckerListener) {
      super(Method.GET, url, null, null);
      this.ipCheckerListener = ipCheckerListener;
   }

   @Override
   protected void deliverResponse(String response) {
      super.deliverResponse(response);
      if (ipCheckerListener != null) {
         try {
            if (new JSONObject(response).getBoolean("is_valid")) {
               ipCheckerListener.onIpChecked(IpStatus.iran);
            } else {
               ipCheckerListener.onIpChecked(IpStatus.outside);
            }
         } catch (JSONException e) {
            e.printStackTrace();
            ipCheckerListener.onIpChecked(IpStatus.failed);
         }
      }
   }

   @Override
   public void deliverError(VolleyError error) {
      super.deliverError(error);
      if (ipCheckerListener != null) {
         ipCheckerListener.onIpChecked(IpStatus.failed);
      }
   }
}

