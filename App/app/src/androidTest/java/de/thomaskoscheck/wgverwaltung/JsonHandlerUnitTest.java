package de.thomaskoscheck.wgverwaltung;

import org.junit.Test;

import de.thomaskoscheck.wgverwaltung.server_communication.Expense;
import de.thomaskoscheck.wgverwaltung.server_communication.ServerResponse;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit test of the json handler
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JsonHandlerUnitTest {
    @Test
    public void parse_server_response_isCorrect() {
        String testJson = "{\"credit\":\"15.4\",\"expenses\":[{\"id\":\"37\",\"product\":\"Grillkohle\",\"requester\":\"Torben\",\"price\":\"2.49\",\"date\":\"2018-06-03\"}]}???????";

        Expense expectedExpense = new Expense("Torben", "Grillkohle", 2.49, 37);
        Expense[] expectedExpenses = new Expense[1];
        expectedExpenses[0] = expectedExpense;

        try {
            ServerResponse serverResponse = JsonHandler.parseServerResponse(testJson);
            assertEquals("15.4", serverResponse.getCredit());
            assertArrayEquals(expectedExpenses, serverResponse.getExpenses());
        } catch (Exception e) {
            e.printStackTrace();
            fail("JsonParser threw exception");
        }
    }
}
