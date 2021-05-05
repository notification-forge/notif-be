//package features
//
//import io.cucumber.java8.En
//import io.cucumber.spring.ScenarioScope
//
//@ScenarioScope
//class CommonStepDefs : En {
//
//    private var data = 0
//
//    init {
//        Given("given test") {
//            data = 1
//            println(data)
//        }
//
//        When("when test") {
//            println("when")
//            println(data)
//        }
//
//        Then("then test") {
//            println("then")
//            println(data)
//            assert(1 == 1)
//            assert(data == 1)
//        }
//    }
//}