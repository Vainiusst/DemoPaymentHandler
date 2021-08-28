package com.paymentHandler.demoPaymentHandler;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoPaymentHandlerApplicationTests {

	private final Controllers controllers = new Controllers();

	@Test
	void indexReturns() {
		try {
			JSONObject json = new JSONObject(controllers.index());
			int actual = json.length();
			int expected = controllers.getNumberOfRoutes() - 1; //-1 because index does not contain description of itself
			assert actual == expected;
		} catch (Exception e) {
			assert false;
		}
	}

	@Test
	void createCasesReturns() {
		int qty = 5;
		try {
			JSONObject json = new JSONObject(controllers.createCases(qty));
			JSONObject jsonInfo = new JSONObject(controllers.getGeneralInfo());
			String message = json.getString("message");
			int numberOfCasesInfo = jsonInfo.getInt("UnresolvedCases");
			assert message.startsWith(String.valueOf(qty))
					&& qty == numberOfCasesInfo;
		} catch (Exception e) {
			assert false;
		}
	}

	@Test
	void getCaseReturns() {
		int qty = 5;
		int attemptCaseId = 2;
		controllers.createCases(qty);
		try {
			JSONObject json = new JSONObject(controllers.getCase(attemptCaseId));
			int result = json.getInt("caseId");
			assert result == attemptCaseId;
		} catch (Exception e) {
			assert false;
		}
	}

	@Test
	void getPaymentReturns() {
		int qty = 5;
		int attemptPaymentId = 2;
		controllers.createCases(qty);
		try {
			JSONObject json = new JSONObject(controllers.getPayment(attemptPaymentId));
			int result = json.getInt("paymentId");
			assert result == attemptPaymentId;
		} catch (Exception e) {
			assert false;
		}
	}

	@Test
	void acceptCaseReturns(){
		int qty = 5;
		int attemptCaseId = 2;
		controllers.createCases(qty);
		String expectedCaseIds = "[1, 3, 4, 5]";
		int expectedCaseNo = 4; //expected case number after one case is accepted
		try {
			JSONObject jsonAccept = new JSONObject(controllers.acceptCase(attemptCaseId));
			JSONObject jsonInfo = new JSONObject(controllers.getGeneralInfo());
			String actualCaseIds = jsonInfo.getString("UnresolvedIds");
			int actualCaseNo = jsonInfo.getInt("UnresolvedCases");
			int actualCaseId = Integer.parseInt(jsonAccept.getString("message").substring(5, 6));

			assert attemptCaseId == actualCaseId
					&& expectedCaseNo == actualCaseNo
					&& actualCaseIds.equals(expectedCaseIds);
		} catch (Exception e){
			assert false;
		}
	}

	@Test
	void rejectCaseReturns() {
		int qty = 5;
		int attemptCaseId = 4;
		controllers.createCases(qty);
		String expectedCaseIds = "[1, 2, 3, 5]";
		int expectedCaseNo = 4; //expected case number after one case is rejected
		try {
			JSONObject jsonAccept = new JSONObject(controllers.rejectCase(attemptCaseId));
			JSONObject jsonInfo = new JSONObject(controllers.getGeneralInfo());
			String actualCaseIds = jsonInfo.getString("UnresolvedIds");
			int actualCaseNo = jsonInfo.getInt("UnresolvedCases");
			int actualCaseId = Integer.parseInt(jsonAccept.getString("message").substring(5, 6));

			assert attemptCaseId == actualCaseId
					&& expectedCaseNo == actualCaseNo
					&& actualCaseIds.equals(expectedCaseIds);
		} catch (Exception e) {
			assert false;
		}
	}
}
