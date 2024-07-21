<script>
	import Header from '$lib/header.svelte';

	let title = 'A easy to use link shortener';
	let short;
	let url;
	let result;

	async function doPost() {
		const res = await fetch('http://localhost:8080/api/v1/links', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				short,
				url
			})
		});

		result = await res.json();
		// result = JSON.stringify(json);
	}
</script>

<svelte:head>
	<title>Kshort · Home</title>
	<meta name="description" content={title} />
</svelte:head>

<Header
	heads={[
		{ href: '/', title: 'Home', current: true },
		{ href: '/about', title: 'About', current: false }
	]}
/>

<div class="m-10 mb-5 text-center text-gray-950 dark:text-gray-50">
	<div class="pb-10 text-3xl font-semibold">
		<h1>{title}</h1>
	</div>
</div>

<div class="flex flex-col items-center justify-center text-center">
	<div class="w-2/5 rounded-sm bg-white p-8 shadow-sm dark:bg-slate-800">
		<h1
			class="mb-2 text-xl font-semibold text-gray-950 duration-500 dark:text-gray-50"
			title="Create shortened url"
		>
			Create shortened url
		</h1>
		<div class="items-center justify-center">
			<input
				class="m-2 w-2/5 rounded-sm border-2 border-slate-800 p-2 py-2 dark:border-slate-600 dark:bg-slate-900"
				placeholder="Link (default is random)"
				bind:value={short}
				id="short-input"
			/>
			<input
				placeholder="https://en.wikipedia.org/wiki/Ada_Lovelace"
				class="m-2 w-2/5 rounded-sm border-2 border-slate-800 p-2 py-2 dark:border-slate-600 dark:bg-slate-900"
				bind:value={url}
				id="url-input"
			/>
		</div>

		<button
			class="dark:white m-2 w-10/12 rounded-sm bg-slate-800 p-2 py-2 text-gray-50 duration-500 hover:bg-slate-700 dark:bg-slate-200 dark:text-gray-950 dark:hover:bg-slate-400"
			type="button"
			on:click={doPost}
		>
			Post it.
		</button>
	</div>
</div>
