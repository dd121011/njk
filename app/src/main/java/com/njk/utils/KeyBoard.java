package com.njk.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoard {
	private static InputMethodManager imm;

    /**
     *
     *
     * @param c
     * @param e
     */
	public static void demissKeyBoard(Context c, EditText e) {
		if (imm == null)
			imm = (InputMethodManager) c
					.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
		e.clearFocus();
	}

    public static void demissKeyBoard(Context c,IBinder token){

        if (imm == null){
            imm = (InputMethodManager)c.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (imm.isActive()){
            imm.hideSoftInputFromWindow(token,0);
        }

    }


	/**
	 *
	 *
	 * @param c
	 * @param e
	 */
	public static void showKeyBoard(Context c, EditText e) {
		if (imm == null)
			imm = (InputMethodManager) c
					.getSystemService(Context.INPUT_METHOD_SERVICE);
        // �����
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
