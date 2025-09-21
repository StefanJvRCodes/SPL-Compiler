# Tree Traversal Engine

Complete implementation for SPL Static Semantic Checker with support for multiple traversal algorithms and visitor pattern.

## Class Overview:

### Core Interfaces
- **ASTNode.java** - Defines the abstract syntax tree node interface with methods for node type, value, children management, unique identifiers, and parent relationships. Includes enums for `TraversalOrder` (PRE_ORDER, POST_ORDER, IN_ORDER) and `VisitResult` (CONTINUE, SKIP_CHILDREN, TERMINATE).

- **TreeWalker.java** - Interface for tree traversal operations. Provides methods to traverse AST nodes using the visitor pattern and configure traversal order (pre-order, post-order, in-order).

- **NodeVisitor.java** - Visitor pattern interface with methods for visiting nodes, handling scope entry/exit events, and managing traversal lifecycle (start/end traversal hooks).

### Implementation Classes
- **BasicASTNode.java** - Concrete implementation of the ASTNode interface. Manages parent-child relationships, supports dynamic addition of children, and provides utility methods for tree structure queries (leaf detection, depth calculation, sibling access).

- **DepthFirstWalker.java** - Complete depth-first traversal implementation supporting all three traversal orders. Features sophisticated scope handling for SPL constructs (functions, procedures, blocks, main, global) with robust error handling and recovery mechanisms.

- **AbstractNodeVisitor.java** - Base visitor class providing default implementations and utility methods. Includes helpers for scope detection, node classification, and common visitor operations. Serves as a foundation for custom visitors.

### Specialized Visitors
- **PrintingVisitor.java** - Debug visitor that provides visual tree representation with proper indentation and configurable display options. Shows node IDs, scope entry/exit messages, and supports depth tracking for debugging traversal issues.

- **NodeCollectorVisitor.java** - Specialized visitor for collecting nodes based on type matching or custom predicates. Supports exact matching, pattern matching (using contains), and provides collection statistics and detailed summaries.

### Demo and Testing
- **TreeTraversalDemo.java** - Comprehensive demonstration program showcasing:
  - Creation of sample SPL program AST structures
  - Different traversal order demonstrations (pre-order, post-order)
  - Node collection capabilities with filtering
  - Unique identifier assignment across the tree
  - Examples of SPL-specific node types (VarDecl, GlobBlock, FuncBlock, ProcBlock, MainBlock)

## Key Features:
- **Multiple Traversal Orders**: Pre-order, post-order, and in-order traversal support
- **SPL-Specific Scope Handling**: Recognizes SPL constructs like global blocks, function blocks, procedure blocks, and main blocks
- **Visitor Pattern**: Extensible visitor architecture for different analysis tasks
- **Node Collection**: Built-in capabilities for filtering and collecting specific node types
- **Error Handling**: Robust error recovery and reporting mechanisms
- **Debug Support**: Comprehensive logging and visualization tools

## Quick Start:
```bash
javac *.java
java TreeTraversalDemo
```

## Testing the Engine:
The demo creates a sample SPL program with:
- Global variable declarations
- Function and procedure blocks with local variables
- Main block structure
- Demonstrates all traversal orders and node collection features

See individual files for detailed API documentation and usage examples.
