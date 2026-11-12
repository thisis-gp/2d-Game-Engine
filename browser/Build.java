import org.teavm.tooling.TeaVMTool;
import org.teavm.vm.TeaVMOptimizationLevel;
import java.io.File;
import java.net.*;
import java.util.*;

public class Build {
    public static void main(String[] a) throws Exception {
        TeaVMTool tool = new TeaVMTool();
        tool.setTargetDirectory(new File(a[0]));
        tool.setLog(new org.teavm.tooling.ConsoleTeaVMToolLog(false));
        tool.setTargetFileName("engine.js");
        tool.setMainClass("web.WebMain");
        tool.setEntryPointName("main");
        tool.setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE);
        tool.setObfuscated(false);
        tool.setDebugInformationGenerated(false);
        tool.setSourceMapsFileGenerated(false);
        List<URL> urls = new ArrayList<>();
        for (int i = 1; i < a.length; i++) urls.add(new File(a[i]).toURI().toURL());
        tool.setClassLoader(new URLClassLoader(urls.toArray(new URL[0]), Build.class.getClassLoader()));
        tool.generate();
        org.teavm.tooling.TeaVMProblemRenderer.describeProblems(tool.getDependencyInfo().getCallGraph(), tool.getProblemProvider(), new org.teavm.tooling.ConsoleTeaVMToolLog(false));
        System.out.println("severe=" + tool.getProblemProvider().getSevereProblems().size());
    }
}
