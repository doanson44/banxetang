package org.xetang.map.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xetang.manager.GameManager;

import android.graphics.Point;
import android.util.Pair;
import android.util.SparseArray;

public class XMLLoader {
	final private static String STAGE_FILE = "data/map/stages.xml";
	final private static String OBJECT_FILE = "data/map/objects.xml";
	
	private static SparseArray<StageDTO> _stagesArray;
	private static SparseArray<ObjectDTO> _objectsArray;

	public static boolean loadAllParameters() {
		return loadAllStages() && loadAllObjects();
	}

	private static boolean loadAllStages() {

		try {
			InputStream inputStream = GameManager.AssetManager.open(STAGE_FILE);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputStream);

			Element ele = doc.getDocumentElement();
			ele.normalize(); // Chuẩn hóa!?

			/*
			 * Gán giá trị của màn chơi hiện tại
			 */
			GameManager.mStage = Integer.parseInt(ele
					.getElementsByTagName("CURRENT_STAGE").item(0)
					.getTextContent());

			NodeList stagesNoteList = ele.getElementsByTagName("STAGE");
			_stagesArray = new SparseArray<StageDTO>(stagesNoteList.getLength());

			Element stageNode;
			StageDTO stage;
			for (int i = 0; i < stagesNoteList.getLength(); i++) {
				stageNode = (Element) stagesNoteList.item(i);

				stage = new StageDTO();
				stage.setId(Integer.parseInt(stageNode
						.getElementsByTagName("ID").item(0).getTextContent()));
				
				stage.setLives(Integer.parseInt(stageNode
						.getElementsByTagName("LIVE").item(0).getTextContent()));

				stage.setObjects(loadStageObjects(stageNode
						.getElementsByTagName("OBJECT")));

				stage.setTanksNameQueue(loadStageTanks(stageNode
						.getElementsByTagName("TANK")));

				_stagesArray.put(stage.getId(), stage);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return true;
	}

	private static List<StageObjectDTO> loadStageObjects(
			NodeList objectsNodeList) {
		List<StageObjectDTO> objects = new ArrayList<StageObjectDTO>(
				objectsNodeList.getLength());
		Element objectNode;
		StageObjectDTO object;

		try {
			for (int i = 0; i < objectsNodeList.getLength(); i++) {
				objectNode = (Element) objectsNodeList.item(i);

				object = new StageObjectDTO();
				object.setId(Integer.parseInt(objectNode
						.getElementsByTagName("ID").item(0).getTextContent()));

				object.setAreas(loadObjectAreas(objectNode
						.getElementsByTagName("AREA")));

				objects.add(object);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return objects;
	}

	private static List<Pair<Point, Point>> loadObjectAreas(
			NodeList areasNodeList) {
		List<Pair<Point, Point>> areas = new ArrayList<Pair<Point, Point>>(
				areasNodeList.getLength());
		Element areaNode;

		try {
			for (int i = 0; i < areasNodeList.getLength(); i++) {
				areaNode = (Element) areasNodeList.item(i);

				areas.add(new Pair<Point, Point>(new Point(Integer
						.parseInt(areaNode.getElementsByTagName("X").item(0)
								.getTextContent()), Integer.parseInt(areaNode
						.getElementsByTagName("Y").item(0).getTextContent())),
						new Point(Integer.parseInt(areaNode
								.getElementsByTagName("WIDTH").item(0)
								.getTextContent()), Integer.parseInt(areaNode
								.getElementsByTagName("HEIGHT").item(0)
								.getTextContent()))));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return areas;
	}

	private static List<String> loadStageTanks(NodeList tanksNodeList) {
		List<String> tanks = new ArrayList<String>(tanksNodeList.getLength());
		Element tankNode;

		try {
			for (int i = 0; i < tanksNodeList.getLength(); i++) {
				tankNode = (Element) tanksNodeList.item(i);

				tanks.add(tankNode.getTextContent());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tanks;
	}

	private static boolean loadAllObjects() {

		try {
			InputStream inputStream = GameManager.AssetManager
					.open(OBJECT_FILE);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputStream);

			Element ele = doc.getDocumentElement();
			ele.normalize(); // Chuẩn hóa!?

			NodeList objectsNoteList = ele.getElementsByTagName("OBJECT");
			_objectsArray = new SparseArray<ObjectDTO>(objectsNoteList.getLength());

			Element objectNode;
			ObjectDTO object;
			for (int i = 0; i < objectsNoteList.getLength(); i++) {
				objectNode = (Element) objectsNoteList.item(i);

				object = new ObjectDTO();
				object.setId(Integer.parseInt(objectNode
						.getElementsByTagName("ID").item(0).getTextContent()));

				object.setName(objectNode.getElementsByTagName("NAME").item(0)
						.getTextContent());

				object.setTextures(objectNode.getElementsByTagName("TEXTURE")
						.item(0).getTextContent());

				_objectsArray.put(object.getId(), object);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return true;
	}

	public static StageDTO getStage(int stageID) {
		return _stagesArray.get(stageID);
	}

	public static ObjectDTO getObject(int objectID) {
		return _objectsArray.get(objectID);
	}
}
