/**
 * NodeCollectorVisitor.java
 * Visitor implementation for collecting nodes by type or criteria
 */

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Visitor for collecting nodes based on type or custom criteria
 */
class NodeCollectorVisitor extends AbstractNodeVisitor {
    private final String targetNodeType;
    private final Predicate<ASTNode> customPredicate;
    private final List<ASTNode> collectedNodes;
    private boolean collectByType;
    
    /**
     * Constructor for collecting nodes by exact type match
     * @param nodeType The exact node type to collect
     */
    public NodeCollectorVisitor(String nodeType) {
        this.targetNodeType = nodeType;
        this.customPredicate = null;
        this.collectedNodes = new ArrayList<>();
        this.collectByType = true;
    }
    
    /**
     * Constructor for collecting nodes using a custom predicate
     * @param predicate Custom predicate to determine which nodes to collect
     */
    public NodeCollectorVisitor(Predicate<ASTNode> predicate) {
        this.targetNodeType = null;
        this.customPredicate = predicate;
        this.collectedNodes = new ArrayList<>();
        this.collectByType = false;
    }
    
    /**
     * Constructor for collecting nodes by type pattern (contains match)
     * @param nodeTypePattern Pattern to match against node types
     * @param usePatternMatching If true, uses contains matching; if false, exact match
     */
    public NodeCollectorVisitor(String nodeTypePattern, boolean usePatternMatching) {
        this.targetNodeType = nodeTypePattern;
        this.collectedNodes = new ArrayList<>();
        this.collectByType = true;
        
        if (usePatternMatching) {
            this.customPredicate = node -> 
                node.getNodeType().toLowerCase().contains(nodeTypePattern.toLowerCase());
            this.collectByType = false;
        } else {
            this.customPredicate = null;
        }
    }
    
    @Override
    public VisitResult visitNode(ASTNode node) {
        boolean shouldCollect = false;
        
        if (collectByType) {
            // Exact type matching
            shouldCollect = node.getNodeType().equals(targetNodeType);
        } else if (customPredicate != null) {
            // Custom predicate matching
            shouldCollect = customPredicate.test(node);
        }
        
        if (shouldCollect) {
            collectedNodes.add(node);
        }
        
        return VisitResult.CONTINUE;
    }
    
    /**
     * Get a copy of the collected nodes
     * @return List of collected nodes (defensive copy)
     */
    public List<ASTNode> getCollectedNodes() {
        return new ArrayList<>(collectedNodes);
    }
    
    /**
     * Get the count of collected nodes
     * @return Number of nodes collected
     */
    public int getCollectedCount() {
        return collectedNodes.size();
    }
    
    /**
     * Clear the collection of nodes
     */
    public void clearCollection() {
        collectedNodes.clear();
    }
    
    /**
     * Check if any nodes were collected
     * @return true if collection is not empty
     */
    public boolean hasCollectedNodes() {
        return !collectedNodes.isEmpty();
    }
    
    /**
     * Print summary of collected nodes
     */
    public void printCollectionSummary() {
        System.out.printf("Collected %d nodes%n", collectedNodes.size());
        
        if (!collectedNodes.isEmpty()) {
            // Group by type and count
            java.util.Map<String, Long> typeCounts = collectedNodes.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    ASTNode::getNodeType,
                    java.util.stream.Collectors.counting()));
            
            System.out.println("Node type distribution:");
            typeCounts.entrySet().stream()
                     .sorted(java.util.Map.Entry.<String, Long>comparingByValue().reversed())
                     .forEach(entry -> 
                         System.out.printf("  %s: %d%n", entry.getKey(), entry.getValue()));
        }
    }
}