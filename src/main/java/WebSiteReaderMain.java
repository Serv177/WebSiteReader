import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebSiteReaderMain {

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.out.println("Wrong number of Arguments");
            return;
        }

        List<CustomPair<String, Status>> subpages1 = new ArrayList<>();
        subpages1.add(new CustomPair(args[0], Status.NOT_FOUND));

        List<CustomPair<String, Status>> subpages2 = new ArrayList<>();
        subpages2.add(new CustomPair<>(args[1], Status.NOT_FOUND));

        WebSiteCompare.iterateThroughWebsitesAndCompare(subpages1, subpages2);
        WebSiteCompare.printResult(subpages1, subpages2);

    }


}
