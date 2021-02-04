package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.Listenes.DataChangerListeners;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidadeExcptions;
import model.service.SellerService;

public class SellerFormController implements Initializable {

	private List<DataChangerListeners> dataChangerListeners = new ArrayList<>();

	private Seller entity;

	private SellerService service;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErroName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	public void subcribleDataChangerListener(DataChangerListeners listener) {
		dataChangerListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangelistener();
			Utils.currentStage(event).close();

		} catch (ValidadeExcptions e) {
			setErrorMessages(e.getErros());

		} catch (DbException e) {
			Alerts.showAlert("Erro save object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangelistener() {
		for (DataChangerListeners listener : dataChangerListeners) {
			listener.onDataChanger();
		}

	}

	private Seller getFormData() {

		Seller obj = new Seller();

		ValidadeExcptions exception = new ValidadeExcptions("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addErro("Name", "Field Can´t be empty");
		}
		obj.setName(txtName.getText());

		if (exception.getErros().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	private void initializeNode() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

	private void setErrorMessages(Map<String, String> error) {
		Set<String> fields = error.keySet();

		if (fields.contains("Name")) {
			labelErroName.setText(error.get("Name"));
		}
	}

}
