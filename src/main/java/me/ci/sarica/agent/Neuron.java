package me.ci.sarica.agent;

import java.util.ArrayList;

/**
 * Created by thedudefromci on 10/14/17.
 *
 * This class represents a single neuron in an agent. This object holds a current value, has incoming
 * and outgoing connections, has a 'position' in space, has a custom activation function, and can
 * release and respond to changes in chemicals.
 */
public class Neuron
{
    private final ArrayList<Connection> incomingConnections = new ArrayList<Connection>();
}
