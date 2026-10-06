# NOTES.md

## Summary of Changes

I focused on the highest-value correctness, reliability, performance, and UX issues.

- Fixed SQL `AND`/`OR` precedence in `TaskRepository` so archived tasks and status filtering are applied correctly.
- Reset pagination to page 1 when search or status filters change.
- Added validation for invalid task status values and invalid pagination parameters.
- Improved frontend loading and error handling in `useTasks`.
- Removed the artificial `Thread.sleep()` delay from the backend.
- Prevented stale asynchronous API responses from overwriting newer search results.
- Changed task ordering to ascending ID as a UX improvement.

## What I Chose Not to Change

I did not add new features or rewrite the existing architecture. I also did not make broader database/indexing, authentication, or UI changes because they were outside the scope of this small exercise and could introduce unnecessary changes.

## Biggest Remaining Risk

The biggest remaining risk is that pagination is performed in memory after fetching all matching tasks from the database. This may become slow and consume more memory as the number of tasks grows. A production implementation should use database-level pagination with Spring Data `Pageable`.

## Tools / AI Used

I used ChatGPT to help review the React and Spring Boot code, identify potential bugs, explain their root causes, and suggest focused fixes. I reviewed the suggestions, applied the changes manually, and tested the application to understand and verify each change.