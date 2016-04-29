package com.kgfsl.nlcsampleproject;

import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;

public class NlcSample {

	public static final String LANGUAGE_EN = "en";

	public static void main(String[] args) throws UnsupportedEncodingException, JSONException {
		NaturalLanguageClassifier service = new NaturalLanguageClassifier();
		service.setUsernameAndPassword("c4a4fcc1-2d95-47b4-9c7b-840d60d6b3ae", "lyyo1g1NDJP8");

		
		// Call the service and get the classification
		
		
		
		//List<TrainingData> classi=new ArrayList<TrainingData>();
		/*File file=new File("D:\\IBM_WORKSPACE\\NLCSampleProject\\src\\resource\\question.csv");
		
		List<TrainingData> trainingData=TrainingDataUtils.fromCSV(file, CSVFormat.DEFAULT);
		System.out.println("TRAINING DATA:::::::"+trainingData.toString());
		
		Classifier c=service.createClassifier("question_data", LANGUAGE_EN, trainingData);
		System.out.println("CREATE CLASSIFIER:::::::::::::"+c);*/
		
		/*String Classifiers =service.getClassifiers().toString();
		System.out.println(Classifiers);*/
		
		/*Classification classification = service.classify("5E00F7x2-nlc-749",
				"Will it be uncomfortably hot?");
		*/
		
		
		/*CREATE CLASSIFIER..........
		 
		File file=new File("D:\\EDA_WS_WITH_DIALOG_SERVICES\\eda-web-service\\src\\main\\resources\\travel_classifier.csv");
		
		List<TrainingData> trainingData=TrainingDataUtils.fromCSV(file, CSVFormat.DEFAULT);
		System.out.println("TRAINING DATA:::::::"+trainingData.toString());
		
		Classifier c=service.createClassifier("eda_classifier", LANGUAGE_EN, trainingData);
		System.out.println("CREATE CLASSIFIER:::::::::::::"+c);
		System.out.println("CREATED.....");
		*/
		
		String Classifiers =service.getClassifier("3B015Fx14-nlc-2017").toString();
		//String Classifiers =service.getClassifiers().toString();
		System.out.println(Classifiers);
		
		//Classification classification=service.classify("5E00F7x2-nlc-749", "Will it be uncomfortably hot?");
		//service.deleteClassifier("5E00F7x2-nlc-749");
		//System.out.println("CLASSIFICATION::::::"+classification.getClasses().size());
		
		/*List<ClassifiedClass> cla=classification.getClasses();
		System.out.println(cla);
		System.out.println(cla.size());
		
		
		String clas="";
				
		for(int i=0;i<cla.size();i++)
		{
			clas=clas.concat(cla.get(i).toString()+"@");
			
		}
		System.out.println(clas);
		
		String[] split=clas.split("@");
		System.out.println(split.length);
		
		for(int i=0;i<split.length;i++)
		{
			System.out.println("***"+split[i]);
			List<ClassifiedClass> list=new ArrayList<ClassifiedClass>();
			
			
		}
		
		List<ClassifiedClass> c=new ArrayList<>(cla);
		System.out.println(c); */
		/*List<String> list=new ArrayList<>();
		for(int i=0;i<classification.getClasses().size();i++)
		{
			list.add(i, classification.getClasses().get(i).toString());
		}
		System.out.println("JSON:::::::::"+list.toString());
		
		
		for(int i=0;i<list.size();i++)
		{
			//System.out.println("LIST::::::"+list.get(i));
			String abc=list.get(i).toString();
			JSONObject jabc = new JSONObject(abc);
			String c=jabc.getString("class_name");
			c=c.concat(String.valueOf(jabc.getDouble("confidence")));
			
			//c.concat(jabc.getDouble("confidence"));
			System.out.println("CLASS_NAME:::"+c);
		}*/
		
		
	// list.add()

	System.out.println("********************************");
	
	/*String st=classification.getClasses().toString();
	
	System.out.println(st);*/
	
	//String nlcHtml = parseNlcStringToHtml(st);

	//System.out.println(nlcHtml);	
		
	}
	
	// "<div>temprature - 98%<p>condition - 21%</div>";
	public static String parseNlcStringToHtml(String sInput) throws JSONException
	{
		String sReturnHtml = "<div>";
		String[] myArr = sInput.split("},");
		
		for (int i = 0; i < myArr.length; i++) {
			String sPurified = myArr[i].replace("[{", "").replace("}]", "").replace("{", "");
			sPurified = "{" + sPurified + "}";
			
			JSONObject myJson = new JSONObject(sPurified);
			sReturnHtml += "<p>" + myJson.getString("class_name") + " - " +  myJson.getString("confidence").substring(0, 4) ;				
		}
		sReturnHtml += "</div>";
		
		return sReturnHtml;
	}
	

}
