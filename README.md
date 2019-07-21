# HuskyBundle


## 用途

	節省接收端麻煩的getIntent or getArgument等取值過程


## 建立依賴


1.build.gradle(Project)

		allprojects {
  		  repositories {
	       		 .
	      		 .
	      		 .
	      	  maven { url 'https://jitpack.io' }
 		   }
		}



2.build.gradle(app)

	dependencies {
  		  	.
  		  	.
   			.
  	  implementation 'com.github.noel77543:HuskyBundle:v1.0.3'
	}




## 使用方法

	在接收端頁面變數加上Annotation - @GetValue
	並在Activity or Fragment 初始化處呼叫 
	HuskyBundle.getInstance().inject(this);
	始完成賦值。




## 情境示例



[ 情境 - MainActivity 傳值 SecondActivity  與 MainActivity 傳值 ThirdActivity]

    MainActivity{

      //指定目標
      @PutValue(target = "SecondActivity")
      private String test1;
      //指定多個目標
      @PutValue(target = {"SecondActivity", "ThirdActivity"})
      private ArrayList<TestModel> testModels;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);

	//賦值給test1 與 testModels
	test1 = "ttt";
          TestModel testModel = new TestModel();
          testModel.setName("Noel");
          testModels = new ArrayList<>();
          testModels.add(testModel);	
	
	//前往SecondActivity
	view1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
	          HuskyBundle.getInstance().take(MainActivity.this, new SecondActivity());
	      }
	 });
	 
	//前往ThirdActivity    
	view2.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
	          HuskyBundle.getInstance().take(MainActivity.this, new ThirdActivity());
	      }
	 });   
	    
      }
    }

    SecondActivity{
	
         //加入Annotation
         //須注意接收端變數名稱需與傳送端key值相符  
	@GetValue
	private ArrayList<TestModel> testModels;
         //亦可透過標註符"name" 指定為與傳遞端key值相符的名稱後即可任意更改變數名稱
	@GetValue(name = "test1")
	String stringTest;
	
	@Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        HuskyBundle.getInstance().inject(this);
	      
	        //TODO Noel : 至此完成接值 可自行嘗試Log出來...

	
	}
    }


    ThirdActivity{

         //加入Annotation
         //須注意接收端變數名稱需與傳送端key值相符  
	@GetValue
	private ArrayList<TestModel> testModels;
         //亦可透過標註符"name" 指定為與傳遞端key值相符的名稱後即可任意更改變數名稱
	@GetValue(name = "test1")
	String stringTest;
	
	@Override
	  protected void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        HuskyBundle.getInstance().inject(this);
	      
	        //TODO Noel : 至此完成接值 可自行嘗試Log出來... 
	        //但你會發現testModels 是null ，因為在MainActivity時該變數傳送目標並沒有指定ThirdActivity

	}
    }	
	
	
	
	
	
	



## 其它

更多使用方式請參閱![範例教學](https://github.com/noel77543/Demo_HuskyBundle)
	
	

	
	
