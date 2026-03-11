# ICONIX Process

The ICONIX Process is a structured approach to software development that emphasizes collaboration, communication, and continuous improvement. It is based on the principles of Agile software development and Lean manufacturing, and it is designed to help teams deliver high-quality software products that meet the needs of their customers.

Steps of ICONIX Process:
- step 1: Get business requirements
- step 2: Identify real world domain objects (domain modeling)
- step 3: Define the behavrioral requirements (use case modeling)
- step 4: Perform robustness analysis to disambiguate the use cases and identify gaps in the domain model
- step 5: Asslocate behavior to your objects (sequence diagram)
- step 6: Finish the static model (class diagram)
- step 7: Write/generate the code(source code)
- step 8: Perform system and user-acceptance testing.

## 1. Get business requirements
Define what the systme should be capable of doing. Get the business requirements from the customer or a team of business analysts.

## 2. Identify real world domain objects (domain modeling)
Domain modeling: Understand the problem space in unambiguous terms. 

### 2.1 Goal of domain modeling
The goal is to build **a gossary of object names** that will serve as the nouns within your **use case text**.

### 2.2 Subdivided steps
This step can be further subdivided into:
- step 1: identity real world domain objects and the generalization and aggregation among those objects. start drawing a high level class diagram, and generate a gossary of object names with definitions.
- step 2: Do some rapid prototyping of the proposed systemto validate the domain model until it is satisfied.

How do you identify objects to go in the domain model?

the most important of which is to use your own experience and judgement. a good starting points is to use the grammatical inspection technique: quickly pass through the business requirements, highlightig nous and verbs as you go. Nous and noun phrases become classes and attributes, verbs and verb phrases become operations and associations, prossessive phrases indicate that nouns shoulbe be attributes rathen than classes.

### 2.3 Check List for domain model
- Is the domain model clear and unambiguous?
- Is there a gossary of object names with definitions?

## 3. Define the behavrioral requirements (use case modeling)
Use case: Define how the actor and systme will interact.
### 3.1 Goal of use case modeling
Describe the system usage in the context of the object model.
### 3.2 Subdivided steps
This step can be further subdivided into:
- step 1: Identify the actors
- step 2: Identify the use cases by asking "What does the actor want to do?"
- step 3: Draw the use case diagrams
- step 4: organize the user cases into groups. Capture this organization in a package diagram.
- step 5: Write the use case text for each use case using the domain objects identified in step 2.
- step 6: Do some rapid prototyping of the proposed systemto validate the use case model until it is satisfied.
### 3.3 Check List for use case model
How do you know when you've finished use case modeling?
- You've built use cases that together account for all the system's desired functionality.
- You've produced clear and concise written descriptions of the basic course of action, along with appropriate alternative courses of action, for each use case.
- You've factored out scenarios common to more than one use case, using the precedes and invokes constructs.

## Milestone 1: Requirements Review

## 4. Perform robustness analysis to disambiguate the use cases and identify gaps in the domain model
A graphic of use case text to disambiguate the use cases text and identify gaps in the domain model.
### 4.1 Goal of robustness analysis
Discover missing objects and operations, and to disambiguate the use cases text.
### 4.2 Subdivided steps
This step can be further subdivided into:
- step 1: Draw robustness diagrams. For each use case.
a. Identify the boundary object(s) for the use case.
b. Identify the controller object(s) for the use case.
c. Identify the entity object(s) for the use case.
d. update domain model class diagram with new objects and attributes as you discover them.
e. Disambiguate the use case text so that is matches the robustness diagram.
- step 2: Finish updating the class diagram so that it reflects the completion of the analysis phase.
### 4.3 Check List for robustness analysis
How do you know when you're finished with robustness analysis?
- Have you covered all of your alternate courses?
- Have you identifed all of the methods/functions?
- have you mapped all data flows between entities?
- have you updated the domain model class diagram with new objects and attributes as you discover them?
- have you disambiguated the use case text so that is matches the robustness diagram?
- have you done some rapid prototyping of the proposed systemto validate the robustness diagram until it is satisfied?

## Milestone 2: Preliminary Design Review

## 5. Allocate behavior to your objects (sequence diagram)
### 5.1 Goal of allocation
### 5.2 Subdivided steps
This step can be further subdivided into:
- step 1: Identify the message that need to be passed between objects and the associated methods to be invoked.
- step 2: Draw a sequence diagram with use case text running down the left side and design information on the right.
- step 3: Continue to update the class diagrams with attributes and operations as you find them.
### 5.3 Check List for allocation

## 6. Finish the static model (class diagram)
### 6.1 Goal of static model
### 6.2 Subdivided steps
This step can be further subdivided into:
- step 1: Add detailed design information.
- step 2: verify with your team that your design satisfies all the requirments of the use cases.
### 6.3 Check List for static model

# Milestone 3: Detailed Design Review

## 7. Write the code
### 7.1 Goal of code writing
### 7.2 Subdivided steps
This step can be further subdivided into:
- step 1: Generate test code
- step 2: Write/generate the code.
- step 3: perform unit and integration testing.
### 7.3 Check List for code writing

## 8. Perform system and user-acceptance testing
system and user-acceptance testing.

## Milestone 4: Delivery
