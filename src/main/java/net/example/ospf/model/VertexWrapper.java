package net.example.ospf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public
class VertexWrapper<String> implements Comparable<VertexWrapper<String>> {
    private final String node;
    private int totalDistance;
    private VertexWrapper<String> predecessor;

    @Override
    public int compareTo(VertexWrapper<String> o) {
        return Integer.compare(this.totalDistance, o.totalDistance);
    }

}
