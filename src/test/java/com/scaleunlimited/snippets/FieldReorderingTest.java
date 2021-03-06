package com.scaleunlimited.snippets;

import org.junit.Test;

import cascading.flow.local.LocalFlowConnector;
import cascading.flow.local.LocalFlowProcess;
import cascading.operation.Debug;
import cascading.operation.Identity;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntryCollector;

import com.scaleunlimited.cascading.NullSinkTap;
import com.scaleunlimited.cascading.local.InMemoryTap;

public class FieldReorderingTest {

    @Test
    public void test() throws Exception {
        Fields sourceFields = new Fields("a", "b", "c");
        InMemoryTap inTap = new InMemoryTap(sourceFields);
        TupleEntryCollector writer = inTap.openForWrite(new LocalFlowProcess());
        writer.add(new Tuple(1, "a", "first"));
        writer.add(new Tuple(2, "b", "second"));
        writer.add(new Tuple(3, "c", "third"));
        writer.close();

        Pipe pipe = new Pipe("in");
        pipe = new Each(pipe, new Debug("before", true));
        pipe = new Each(pipe, new Fields("c", "a"), new Identity());
        pipe = new Each(pipe, new Debug("after", true));

        new LocalFlowConnector().connect(inTap, new NullSinkTap(), pipe).complete();
    }

}
