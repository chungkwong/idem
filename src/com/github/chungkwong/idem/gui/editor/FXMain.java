/*
 * Copyright (C) 2016 Chan Chung Kwong <1m02math@126.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.chungkwong.idem.gui.editor;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;
import javafx.stage.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class FXMain extends Application{
	@Override
	public void start(Stage primaryStage){
		BorderPane root=new BorderPane();
		Button back=new Button("<");
		Button forward=new Button(">");
		TextField location=new TextField("file:///home/kwong/javatool/html/zh_CN/api/index.html");
		WebView browser=new WebView();
		back.setOnAction((event)->browser.getEngine().getHistory().go(-1));
		forward.setOnAction((event)->browser.getEngine().getHistory().go(+1));
		location.setOnAction((event)->browser.getEngine().load(location.getText()));
		root.setTop(new BorderPane(location,null,null,null,new HBox(back,forward)));
		root.setCenter(browser);
		Scene scene=new Scene(root);
		primaryStage.setTitle("Webkit包装");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args){
		launch(args);
	}
}