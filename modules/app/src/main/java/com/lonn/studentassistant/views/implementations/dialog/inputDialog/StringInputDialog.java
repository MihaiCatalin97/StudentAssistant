package com.lonn.studentassistant.views.implementations.dialog.inputDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Setter;
import lombok.experimental.Accessors;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lonn.studentassistant.utils.Utils.displayWidth;

@Accessors(chain = true)
public class StringInputDialog extends Dialog {
	@Setter
	private String title;
	@Setter
	private String inputTitle;
	@Setter
	private String inputHint;
	@Setter
	private String positiveButtonText;
	@Setter
	private Consumer<String> positiveButtonAction;

	public StringInputDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.string_input_dialog);

		if (getWindow() != null) {
			getWindow().setLayout((int) (displayWidth * 0.8), WRAP_CONTENT);
		}

		((TextView) findViewById(R.id.stringInputDialogInputEditText)).setHint(inputHint);
		((TextView) findViewById(R.id.stringInputTitle)).setText(title);
		((TextView) findViewById(R.id.stringInputDialogInputTitle)).setText(inputTitle);
		((TextView) findViewById(R.id.buttonPositive)).setText(positiveButtonText);

		findViewById(R.id.buttonNegative).setOnClickListener(view -> dismiss());

		findViewById(R.id.buttonPositive).setOnClickListener(view -> {
			if (positiveButtonAction != null) {
				positiveButtonAction.consume(((EditText) findViewById(R.id.stringInputDialogInputEditText)).getText().toString());
			}

			dismiss();
		});
	}
}
