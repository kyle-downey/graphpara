/*
 * (C) Copyright 2017 Kyle F. Downey.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cloudwall.graph.io.graphviz;

import cloudwall.graph.io.ReaderInput;
import cloudwall.graph.io.graphviz.GraphvizDotModel.*;

import org.javafp.parsecj.input.Input;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.*;

// bunch of problems still with Graphviz parsing
@Ignore
public class GraphvizDotParserTest {
    @Test
    public void parseCluster() throws Exception {
        parseModel("cluster.dot");
    }

    @Test
    public void parseCrazy() throws Exception {
        parseModel("crazy.dot");
    }

    @Test
    public void parseDataStructure() throws Exception {
        parseModel("datastruct.dot");
    }

    @Test
    public void parseErDiagram() throws Exception {
        parseModel("er.dot");
    }

    @Test
    public void parseNestedClusters() throws Exception {
        parseModel("fdpclust.dot");
    }

    @Test
    public void parseFiniteStateModel() throws Exception {
        parseModel("fsm.dot");
    }

    @Test
    public void parseHelloWorld() throws Exception {
        GraphvizDotModel model = parseModel("helloworld.dot");
        EdgeStatement edge = (EdgeStatement) model.getStatements().iterator().next();
        NodeId lhs = (NodeId) edge.getLhsTerminal();
        NodeId rhs = (NodeId) edge.getRhsTerminals().iterator().next();

        assertEquals("G", String.valueOf(model.getId()));
        assertFalse(model.isStrict());
        assertTrue(model.isDigraph());
        assertEquals(1, model.getStatements().size());
        assertEquals("Hello", lhs.getNodeId().toString());
        assertNull(lhs.getPortId());
        assertNull(lhs.getCompassPoint());
        assertEquals(EdgeOp.DIRECTED, edge.getOperator());
        assertEquals("World", rhs.getNodeId().toString());
        assertNull(lhs.getPortId());
        assertNull(lhs.getCompassPoint());
        assertTrue(edge.getAttributes().isEmpty());
    }

    @Test
    public void parseProcessTree() throws Exception {
        parseModel("process.dot");
    }

    @Test
    public void parseProfilerData() throws Exception {
        parseModel("profile.dot");
    }

    @Test
    public void parseSoftwareMaint() throws Exception {
        parseModel("softmaint.dot");
    }

    @Test
    public void parseSwitch() throws Exception {
        parseModel("switch.dot");
    }

    @Test
    public void parseTwoPi() throws Exception {
        parseModel("twopi.dot");
    }

    @Test
    public void parseWorld() throws Exception {
        parseModel("world.dot");
    }

    @Test
    public void parseCompassPoints() throws Exception {
        for (GraphvizDotModel.CompassPoint point : GraphvizDotModel.CompassPoint.values()) {
            GraphvizDotParser.compassPoint().parse(Input.of(point.getLabel()));
        }
    }

    @Test
    public void parseNodeId() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(Input.of("A_BC")).getResult();
        assertEquals("A_BC", nodeId.toString());
    }

    @Test
    public void parseNodeIdInt() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(Input.of("123")).getResult();
        assertEquals("123", nodeId.toString());
    }

    @Test
    public void parseNodeIdPortIdBothInt() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(Input.of("123:456")).getResult();
        assertEquals("123:456", nodeId.toString());
    }

    @Test
    public void parseNodeIdWithPort() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(Input.of("\"X yz\":32")).getResult();
        assertEquals("\"X yz\":32", nodeId.toString());
    }

    @Test
    public void parseNodeIdWithCompassPoint() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(Input.of("x64:nw")).getResult();
        assertEquals("x64:nw", nodeId.toString());
    }
    
    @Test
    public void parseNodeIdWithPortAndCompassPoint() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(Input.of("ABC:123:_")).getResult();
        assertEquals("ABC:123:_", nodeId.toString());
    }

    private GraphvizDotModel parseModel(String resource) throws Exception {
        InputStream in = getClass().getResourceAsStream(resource);
        Reader r = new InputStreamReader(in);
        ReaderInput input = new ReaderInput(r);
        return GraphvizDotParser.newInstance().parse(input.toInput()).getResult();
    }
}