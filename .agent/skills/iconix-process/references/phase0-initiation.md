# Phase 0: Project Initiation Plan

Establish project scope, goals, and a high-level roadmap through a collaborative, interactive process.

## 1. Domain Research (Pre-Interview)
Before asking the user the first discovery question, you MUST perform foundational domain research.
- **Mandatory Search**: Use `search_web` to research the project's domain, common industry standards, and typical business challenges.
- **Objectives**: Identify standard user personas, high-level business rules, and frequent "pain points" in this space.
- **Rationale**: Entering an interview with zero domain knowledge leads to generic questions. Research allows you to ask "informed" questions that demonstrate competence and uncover nuanced requirements faster.

## 2. Discover Project Purpose (Conversation)
The primary goal of this iteration is to understand the "Big Picture" through an exhaustive, collaborative dialogue informed by your research.
- **Phased Discovery Mandate**: You MUST conduct a minimum of **3-5 distinct question-and-answer rounds** before drafting the official Project Description.
- **Informed Inquiries**: Ground your questions in your research findings (e.g., "Industry standards for [Domain] suggest [X] is a common challenge. How does your project approach this?").
- **Ask Targeted Questions**: Focus on the *Problem*, *Users*, *Business Value*, and *Critical Success Factors*.
- **One Question at a Time**: Keep interactions micro-focused. If a topic is complex, break it into a dedicated thread of questions.
- **Rationale**: Short discovery leads to shallow understanding. Multi-round dialogue uncovers "unknown unknowns" that are vital for robust modeling in Phase 1.

## 3. Discovery Synthesis (Interactive Summary)
Before drafting the Project Description, you MUST present a "Discovery Synthesis" to the user.
- **Summarize Key Findings**: State your current understanding of the problem, goals, and constraints.
- **Seek Missing Gaps**: explicitly ask: "Is there anything critical I've missed or any edge cases we haven't touched upon yet?"
- **Rationale**: This is the final "sanity check" to ensure the conceptual model is complete before committing to a formal scope.

## 4. Draft & Sign-off Project Description
Only once synthesis is complete and the discovery threshold is met:
- **Draft Project Description**: Present a 2-3 sentence authoritative description.
- **Confirm & Sign-off**: Do not proceed to requirement elicitation until the user explicitly approves this description.

## 5. Elicit Business Requirements
Once the purpose is confirmed, drill down into specific requirements.
- **Focus on "The What"**: Ask about business rules, constraints, and user needs.
- **Avoid "The How"**: Do not ask about technical systems, schemas, or protocols yet.
- **Sequential Questioning**: Ask clarifying questions **one by one** in the console.

## 6. Presenting the Design
- Once you believe you understand what you're building, present the product description to the user.
- Break it into sections of 200-300 words
- Ask after each section whether it looks right so far
- Be ready to go back and clarify if something doesn't make sense

## 7. Define Roadmap
Identify the high-level phases and milestones.
- **List Use Cases**: Capture potential use cases as they emerge, but do not model them yet.
- **Set Expectations**: Clarify that coding only happens in Phase 4.

## 8. Save & Loop
- **Persist State**: Write findings to `phase0-plan-v{N}.md`.
- **Silent Save**: After the initial mention at the start of Phase 0, save these files silently.
- **Continue**: Loop iterations until the user says "continue".
