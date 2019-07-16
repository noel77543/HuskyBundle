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



[ 情境 - 傳值Test1Activity -> Test2Activity ]

Test1Activity:

	Intent intent = new Intent(Test1Activity.this, Test2Activity.class);
    Bundle bundle = new Bundle();
    bundle.putString("test","AAA");
    bundle.putBoolean("booleanTest",true);
    intent.putExtras(bundle);
	 //以原生方式攜值前往Test2Activity
    startActivity(intent);


Test2Activity:
	
    //加入Annotation
    //須注意接收端變數名稱需與傳送端key值相符  
	@GetValue
	boolean booleanTest;
    //亦可透過標註符"name" 指定為與傳遞端key值相符的名稱後即可任意更改變數名稱
	@GetValue(name = "test")
	String stringTest;

	
	@Override
	  protected void onCreate(@Nullable Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
		  HuskyBundle.getInstance().inject(this);
				.
				.
				.

	
	}
