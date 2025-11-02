# Smart City Scheduling System
**Student:** Yelzhan Zhandos  
**Group:** SE-2426

## Overview
**Smart City Scheduling System** is a comprehensive implementation of graph algorithms designed for city-service task scheduling with dependency management.  
The system efficiently handles cyclic dependencies through **Strongly Connected Components (SCC)** detection and provides **optimal scheduling** via **topological sorting** and **critical path analysis**.


## Algorithm Performance Analysis

| Algorithm         | Operations | Time (ns)              | Graph Size   | Key Results                               |
|-------------------|-------------|-----------------------:|--------------|--------------------------------------------|
| SCC (Kosaraju)    | 38–46       | 38,500 – 1,124,800     | 6–8 vertices | Detected 6 SCCs, found cycle [1,3,2]       |
| Topological Sort  | 26–32       | 25,200 – 916,100       | 6 vertices   | Component order: [0,4,1,5,2,3]             |
| Shortest Path     | 45          | 531,800                | 6 vertices   | Min distances from source 0                |
| Longest Path     | 82          | 439,800                | 6 vertices   | Longest path: [0→1→3→5], length = 12      |


## Dataset Specifications

| Dataset   | Vertices | Edges | Type         | Characteristics                    |
|------------|-----------|-------|--------------|-------------------------------------|
| small_1    | 6         | 5     | DAG          | Pure directed acyclic graph         |
| small_2    | 6         | 6     | Cyclic       | Single cycle (0→1→2→0)             |
| small_3    | 6         | 7     | Multi-cyclic | Multiple small cycles              |
| medium_1   | 15        | 18    | Mixed        | Linear chain with cross edges       |
| medium_2   | 15        | 15    | Multi-SCC    | Three distinct SCC components       |
| medium_3   | 12        | 22    | Dense        | Highly connected graph              |
| large_1    | 30        | 58    | Sparse       | Random sparse connections           |
| large_2    | 25        | 82    | Dense        | Highly interconnected              |
| large_3    | 35        | 41    | Mixed        | Hierarchical with cross edges       |


## Technical Implementation

### Algorithm Selection Rationale

| Algorithm         | Choice           | Rationale                            | Complexity                |
|-------------------|------------------|--------------------------------------|---------------------------|
| SCC               | Kosaraju         | Simple implementation, predictable   | Time: O(V+E), Space: O(V) |
| Topological Sort  | Kahn’s Algorithm | Natural fit for DAGs, detects cycles | Time: O(V+E), Space: O(V) |
| Shortest Path     | DP over Topo Order | Optimal for DAGs, handles negatives | Time: O(V+E), Space: O(V) |

### Design Decisions
- **Weight Model:** Edge weights represent task durations (1–10 units)
- **Metrics Tracking:** Operation counters and nanosecond timing
- **Modular Architecture:** Separate packages for each algorithm family
- **Error Handling:** Graceful fallbacks for file I/O issues


## Performance Insights

### Time Complexity Analysis

| Algorithm        | Theoretical | Observed     | Scaling Factor              |
|------------------|-------------|--------------|------------------------------|
| SCC              | O(V+E)      | ~40–1120 μs  | Linear with graph size       |
| Topological Sort | O(V+E)      | ~25–916 μs   | Efficient for sparse graphs  |
| Shortest Path    | O(V+E)      | ~532 μs      | Consistent performance       |

### Memory Usage Patterns
- **SCC:** Requires reverse graph storage (2× adjacency lists)
- **Topological Sort:** Minimal queue storage
- **Shortest Path:** Distance and predecessor arrays


## Usage Guidelines

### For Dependency Analysis
- **Use Case:** Detect circular dependencies in task scheduling
- **Algorithm:** SCC (Kosaraju)
- **When:** Initial project planning phase

### For Task Scheduling
- **Use Case:** Determine execution order
- **Algorithm:** SCC → Condensation → Topological Sort
- **When:** After cycle detection, before execution

### For Time Estimation
- **Use Case:** Calculate total project duration
- **Algorithm:** Critical Path Analysis
- **When:** During resource allocation and deadline setting

### Optimization Opportunities

| Scenario              | Optimization          | Impact                     |
|------------------------|-----------------------|-----------------------------|
| Large sparse graphs    | Tarjan's SCC          | Single DFS pass             |
| Memory constraints     | Iterative DFS         | Prevents stack overflow     |
| Real-time updates      | Incremental algorithms | Partial recomputation       |

```
## System Architecture

src/main/java/graph/
├── common/ # Base interfaces and Graph class
├── scc/ # Strongly Connected Components
├── topo/ # Topological Sorting
├── dagsp/ # DAG Shortest Paths
├── model/ # Data structures
└── util/ # Dataset generation and I/O
```

## Key Findings

1. **Cycle Detection Efficiency**
   - Successfully identified cyclic dependencies in `medium_2` dataset
   - Condensation graph reduced 15-vertex graph to 6 components
   - Enabled valid topological ordering of cyclic graphs

2. **Critical Path Identification**
   - Found optimal path: [0→1→3→5] with length = 12
   - Demonstrated practical scheduling optimization
   - Provided clear project timeline boundaries

3. **Scalability Performance**
   - Linear time complexity across all algorithms
   - Efficient handling of graphs up to 35 vertices
   - Robust performance on diverse graph structures

**Conclusion**
The Smart City Scheduling System demonstrates the practical application of graph algorithms for real-world task scheduling.
This implementation achieves:

- Robust cycle detection for dependency management
- Efficient scheduling through topological ordering
- Accurate time estimation via critical path analysis
- Scalable performance across multiple graph structures
- Comprehensive metrics for performance evaluation