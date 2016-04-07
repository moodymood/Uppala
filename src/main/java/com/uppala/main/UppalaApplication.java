package com.uppala.main;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UppalaApplication extends Application {

	Stage stage;
	Scene scene;
	BorderPane root;

	private TrayIcon trayIcon;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		this.stage = stage;
		Platform.setImplicitExit(false);
		configureScene();
		configureStage();	
	}

	private void configureStage() {	
		stage.setScene(this.scene);
		stage.show();
		createTrayIcon(stage);
	}

	private void configureScene() {
		root =  new BorderPane();
		root.setCenter(new Label("test"));
		root.setPrefSize(500,400);
		this.scene = new Scene(root);
	}

	public void createTrayIcon(final Stage stage) {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent t) {
					Platform.runLater(new Runnable() {
						public void run() {
							if (SystemTray.isSupported()) {
								stage.hide();
								showProgramIsMinimizedMsg();
							} else {
								System.exit(0);
							}
						}
					});
				}
			});
			// create a action listener to listen for default action executed on the tray icon
			final ActionListener closeListener = new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			};

			ActionListener showListener = new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Platform.runLater(new Runnable() {
						public void run() {
							stage.show();
	
						}
					});
				}
			};

			// create a popup menu
			PopupMenu popup = new PopupMenu();

			MenuItem showItem = new MenuItem("Show");
			showItem.addActionListener(showListener);
			popup.add(showItem);

			MenuItem closeItem = new MenuItem("Exit");
			closeItem.addActionListener(closeListener);
			popup.add(closeItem);

			String imageURLtoString = "/cloud.png";
			java.awt.Image image = Toolkit.getDefaultToolkit().getImage(UppalaApplication.class.getResource(imageURLtoString));
			int trayIconWidth = new TrayIcon(image).getSize().width;
			System.out.println(trayIconWidth);
			int trayIconHeight = new TrayIcon(image).getSize().height;
			System.out.println(trayIconHeight);
			java.awt.Image resized = image.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
			trayIcon = new TrayIcon(resized, "Uppala", popup);
			trayIcon.setImageAutoSize(true);
			
			//trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(showListener);
			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					System.out.println(e.getButton());
					Platform.runLater(new Runnable() {
						public void run() {
							System.out.println(23);
							stage.show();
						}
					});
				}
			});
			
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println(e);
			}

		}
	}

	public void showProgramIsMinimizedMsg() {
		trayIcon.displayMessage("Message.", "Application is still running.", TrayIcon.MessageType.NONE);
	}



}