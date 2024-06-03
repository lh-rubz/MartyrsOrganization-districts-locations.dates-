module DSProj2 {
	requires javafx.controls;
	requires javafx.base;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml,javafx.base;
}
